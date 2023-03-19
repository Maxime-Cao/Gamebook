using GBReaderCaoM.Domains;
using GBReaderCaoM.Infrastructures.JsonFile;
using NUnit.Framework;
using System.Text;

namespace GBReaderCaoM.Infrastructures.Tests.JsonFile
{
    public class JsonSaveSessionRepositoryTest
    {
        private readonly string _firstBookIsbn = "2-170051-01-5";
        private readonly string _secondBookIsbn = "2-170051-02-3";
        private readonly string _thirdBookIsbn = "2-170051-03-1";

        private readonly int _firstBookCurrentPage = 21;
        private readonly int _secondBookCurrentPage = 143;
        private readonly int _thirdBookCurrentPage = 82;

        private readonly DateTime _firstBookFirstRead = new(2015, 6, 25, 23, 48, 0);
        private readonly DateTime _secondBookFirstRead = new(2018, 5, 12, 22, 33, 11);
        private readonly DateTime _thirdBookFirstRead = new(2022, 12, 12, 12, 12, 12);

        private readonly DateTime _firstBookLastRead = new(2015, 6, 30, 11, 15, 23);
        private readonly DateTime _secondBookLastRead = new(2018, 5, 25, 14, 55, 22);
        private readonly DateTime _thirdBookLastRead = DateTime.Now;

        // Source : https://chat.openai.com/chat => Quand je vide le contenu du fichier de test (méthode CleanFile), un marqueur invisible est présent dedans (précisant l'encodage) donc la taille du fichier même vide n'est jamais égale à 0 donc j'ai besoin de cette information pour déterminer la taille d'un fichier vide
        private readonly int _utf8Bom = Encoding.UTF8.GetPreamble().Length;


        [Test]
        public void CreateRepositoryWithNullPath()
        {
            // Dans ce test, on tente de créer un objet JsonSaveSessionRepository avec un Path "null"
            try
            {
                _ = new JsonSaveSessionRepository(null);
                Assert.Fail("La création d'un objet JsonSaveSessionRepository avec un chemin 'null' devrait déclencher une exception");
            }
            catch (Exception e)
            {
                Assert.That(e.Message, Is.EqualTo("Le chemin du fichier ne doit pas être nul et doit correspondre à un fichier .json"));
            }
        }


        [Test]
        public void CreateRepositoryWithNotAFilePath()
        {
            // Dans ce test, on tente de créer un objet JsonSaveSessionRepository avec un Path qui n'est pas un chemin vers un fichier .json
            try
            {
                _ = new JsonSaveSessionRepository(Path.GetFullPath("../../../Resources"));
                Assert.Fail("La création d'un objet JsonSaveSessionRepository avec un chemin qui n'est pas celui d'un fichier Json devrait déclencher une exception");
            }
            catch (Exception e)
            {
                Assert.That(e.Message, Is.EqualTo("Le chemin du fichier ne doit pas être nul et doit correspondre à un fichier .json"));
            }
        }

        [Test]
        public void LoadCorrectListOfUserReadSessions()
        {
            string filePath = Path.GetFullPath("../../../Resources/LoadSessionsOkWithCorrectListOfUserReadSessions/d170051-session.json");
            try
            {
                JsonSaveSessionRepository repository = new(filePath);
                IList<ICanCreateUserReadSession> collectedSessions = repository.LoadUserReadSessions().ToList();

                Assert.That(collectedSessions, Is.Not.Null);
                Assert.That(collectedSessions, Has.Count.EqualTo(2));

                ICanCreateUserReadSession firstSession = collectedSessions[0];
                ICanCreateUserReadSession secondSession = collectedSessions[1];
                Assert.Multiple(() =>
                {
                    Assert.That(firstSession.CurrentBookIsbn, Is.EqualTo("2-170051-01-5"));
                    Assert.That(firstSession.CurrentPageNumber, Is.EqualTo(21));
                    Assert.That(firstSession.FirstRead, Is.EqualTo(DateTime.Parse("2015-06-25T23:48:00.0000000")));
                    Assert.That(firstSession.LastRead, Is.EqualTo(DateTime.Parse("2015-06-30T11:15:23.0000000")));

                    Assert.That(secondSession.CurrentBookIsbn, Is.EqualTo("2-170051-02-3"));
                    Assert.That(secondSession.CurrentPageNumber, Is.EqualTo(143));
                    Assert.That(secondSession.FirstRead, Is.EqualTo(DateTime.Parse("2018-05-12T22:33:11.0000000")));
                    Assert.That(secondSession.LastRead, Is.EqualTo(DateTime.Parse("2018-05-25T14:55:22.0000000")));
                });
            }
            catch (Exception)
            {
                Assert.Fail("La méthode LoadUserReadSessions ne devrait pas déclencher d'exception car le fichier est accessible et toutes les sessions sont dans un format correct");
            }
        }

        [Test]
        public void LoadSessionsWithErrorSyntaxInFile()
        {
            string filePath = Path.GetFullPath("../../../Resources/LoadSessionsNotOkWithErrorSyntaxInFile/d170051-session.json");
            try
            {
                JsonSaveSessionRepository repository = new(filePath);
                repository.LoadUserReadSessions();
                Assert.Fail("La méthode LoadUserReadSessions devrait déclencher une exception car il y a une erreur de syntaxe dans le fichier contenant les sessions de lecture");
            }
            catch (Exception e)
            {
                Assert.That(e.Message, Is.EqualTo("Erreur durant la lecture du fichier JSON. Veuillez vérifier la syntaxe des éléments du fichier."));
            }
        }

        [Test]
        public void LoadSessionsWithMissingFieldInASession()
        {
            string filePath = Path.GetFullPath("../../../Resources/LoadSessionsOkWithMissingFieldInASession/d170051-session.json");
            try
            {
                JsonSaveSessionRepository repository = new(filePath);
                IList<ICanCreateUserReadSession> collectedSessions = repository.LoadUserReadSessions().ToList();

                Assert.That(collectedSessions, Is.Not.Null);
                Assert.That(collectedSessions, Has.Count.EqualTo(1));

                var collectedSession = collectedSessions[0];

                Assert.Multiple(() =>
                {
                    Assert.That(collectedSession.CurrentBookIsbn, Is.EqualTo("2-170051-02-3"));
                    Assert.That(collectedSession.CurrentPageNumber, Is.EqualTo(143));
                    Assert.That(collectedSession.FirstRead, Is.EqualTo(DateTime.Parse("2018-05-12T22:33:11.0000000")));
                    Assert.That(collectedSession.LastRead, Is.EqualTo(DateTime.Parse("2018-05-25T14:55:22.0000000")));
                });
            }
            catch (Exception)
            {
                Assert.Fail("La méthode LoadUserReadSessions ne devrait pas déclencher d'exception lorsqu'une session dans le fichier a un attribut manquant. La session ne devrait juste pas être reprise dans la liste retournée");
            }
        }

        [Test]
        public void LoadSessionsOkWithBadTypeForNumberPage()
        {
            // Le numéro de page est le seul attribut non string (c'est un entier) donc si la valeur récupérée dans le fichier n'est pas un entier, la conversion vers un entier pourrait poser problème
            // Ce test montre que si le numéro de page d'une session de lecture dans le fichier n'est pas un entier, mais qu'il peut être converti en entier, alors la session sera reprise car considérée comme valide
            string filePath = Path.GetFullPath("../../../Resources/LoadSessionsOkWithBadTypeForNumberPage/d170051-session.json");
            try
            {
                JsonSaveSessionRepository repository = new(filePath);
                IList<ICanCreateUserReadSession> collectedSessions = repository.LoadUserReadSessions().ToList();

                Assert.That(collectedSessions, Is.Not.Null);
                Assert.That(collectedSessions, Has.Count.EqualTo(1));

                var collectedSession = collectedSessions[0];

                Assert.Multiple(() =>
                {
                    Assert.That(collectedSession.CurrentBookIsbn, Is.EqualTo("2-170051-01-5"));
                    Assert.That(collectedSession.CurrentPageNumber, Is.EqualTo(21));
                    Assert.That(collectedSession.FirstRead, Is.EqualTo(DateTime.Parse("2015-06-25T23:48:00.0000000")));
                    Assert.That(collectedSession.LastRead, Is.EqualTo(DateTime.Parse("2015-06-30T11:15:23.0000000")));
                });

            }
            catch (Exception)
            {
                Assert.Fail("La méthode LoadUserReadSessions ne devrait pas déclencher d'exception lorsqu'une session dans le fichier a un attribut d'un mauvais type pouvant être converti dans le type adéquat.");
            }
        }

        [Test]
        public void LoadSessionsNotOkWithBadTypeForNumberPage()
        {
            // Le numéro de page est le seul attribut non string (c'est un entier) donc si la valeur récupérée dans le fichier n'est pas un entier, la conversion vers un entier pourrait poser problème
            // Ce test montre que si le numéro de page d'une session de lecture dans le fichier n'est pas un entier, et qu'il ne peut pas être converti en entier, alors la méthode LoadUserReadSessions devrait déclencher une exception
            string filePath = Path.GetFullPath("../../../Resources/LoadSessionsNotOkWithBadTypeForNumberPage/d170051-session.json");
            try
            {
                JsonSaveSessionRepository repository = new(filePath);
                repository.LoadUserReadSessions();
                Assert.Fail("La méthode LoadUserReadSessions devrait déclencher une exception lorsque qu'une session contient un numéro de page qui n'est pas un entier et qui ne peut pas être converti en entier");
            }
            catch (Exception e)
            {
                Assert.That(e.Message, Is.EqualTo("Erreur durant la lecture du fichier JSON. Veuillez vérifier la syntaxe des éléments du fichier."));
            }
        }

        [Test]
        public void LoadSessionsOkWithInvalidIsbn()
        {
            string filePath = Path.GetFullPath("../../../Resources/LoadSessionsOkWithInvalidIsbn/d170051-session.json");

            try
            {
                JsonSaveSessionRepository repository = new(filePath);
                IList<ICanCreateUserReadSession> collectedSessions = repository.LoadUserReadSessions().ToList();

                Assert.That(collectedSessions, Is.Not.Null);
                Assert.That(collectedSessions, Has.Count.EqualTo(1));

                var collectedSession = collectedSessions[0];

                Assert.Multiple(() =>
                {
                    Assert.That(collectedSession.CurrentBookIsbn, Is.EqualTo("2-170051-02-3"));
                    Assert.That(collectedSession.CurrentPageNumber, Is.EqualTo(143));
                    Assert.That(collectedSession.FirstRead, Is.EqualTo(DateTime.Parse("2018-05-12T22:33:11.0000000")));
                    Assert.That(collectedSession.LastRead, Is.EqualTo(DateTime.Parse("2018-05-25T14:55:22.0000000")));
                });
            }
            catch (Exception)
            {
                Assert.Fail("La méthode LoadUserReadSessions ne devrait pas déclencher d'exception lorsqu'une session dans le fichier a un isbn invalide. La session ne devrait juste pas être reprise dans la liste retournée");
            }
        }

        [Test]
        public void LoadSessionsOkWithInvalidNumberPage()
        {
            string filePath = Path.GetFullPath("../../../Resources/LoadSessionsOkWithInvalidNumberPage/d170051-session.json");
            try
            {
                JsonSaveSessionRepository repository = new(filePath);
                IList<ICanCreateUserReadSession> collectedSessions = repository.LoadUserReadSessions().ToList();

                Assert.That(collectedSessions, Is.Not.Null);
                Assert.That(collectedSessions, Has.Count.EqualTo(2));

                ICanCreateUserReadSession firstSession = collectedSessions[0];
                ICanCreateUserReadSession secondSession = collectedSessions[1];
                Assert.Multiple(() =>
                {
                    Assert.That(firstSession.CurrentBookIsbn, Is.EqualTo("2-170051-01-5"));
                    Assert.That(firstSession.CurrentPageNumber, Is.EqualTo(2));
                    Assert.That(firstSession.FirstRead, Is.EqualTo(DateTime.Parse("2015-06-25T23:48:00.0000000")));
                    Assert.That(firstSession.LastRead, Is.EqualTo(DateTime.Parse("2015-06-30T11:15:23.0000000")));

                    Assert.That(secondSession.CurrentBookIsbn, Is.EqualTo("2-170051-02-3"));
                    Assert.That(secondSession.CurrentPageNumber, Is.EqualTo(143));
                    Assert.That(secondSession.FirstRead, Is.EqualTo(DateTime.Parse("2018-05-12T22:33:11.0000000")));
                    Assert.That(secondSession.LastRead, Is.EqualTo(DateTime.Parse("2018-05-25T14:55:22.0000000")));
                });
            }
            catch (Exception)
            {
                Assert.Fail("La méthode LoadUserReadSessions ne devrait pas déclencher d'exception lorsqu'une session dans le fichier a un numéro de page invalide (entier plus petit que 2). Cette session sera reprise dans la liste retournée mais son numéro de page sera fixé à 2 (plus petite valeur acceptée pour un numéro de page dans une session de lecture)");
            }
        }

        [Test]
        public void LoadSessionsOkWithInvalidFirstReadDateTime()
        {
            string filePath = Path.GetFullPath("../../../Resources/LoadSessionsOkWithInvalidFirstReadDateTime/d170051-session.json");

            try
            {
                JsonSaveSessionRepository repository = new(filePath);
                IList<ICanCreateUserReadSession> collectedSessions = repository.LoadUserReadSessions().ToList();

                Assert.That(collectedSessions, Is.Not.Null);
                Assert.That(collectedSessions, Has.Count.EqualTo(1));

                var collectedSession = collectedSessions[0];

                Assert.Multiple(() =>
                {
                    Assert.That(collectedSession.CurrentBookIsbn, Is.EqualTo("2-170051-01-5"));
                    Assert.That(collectedSession.CurrentPageNumber, Is.EqualTo(21));
                    Assert.That(collectedSession.FirstRead, Is.EqualTo(DateTime.Parse("2015-06-25T23:48:00.0000000")));
                    Assert.That(collectedSession.LastRead, Is.EqualTo(DateTime.Parse("2015-06-30T11:15:23.0000000")));
                });
            }
            catch (Exception)
            {
                Assert.Fail("La méthode LoadUserReadSessions ne doit pas déclencher d'exception lorsqu'une session dans le fichier contient un datetime invalide pour la première lecture du livre. Cette session n'est tout simplement pas reprise dans la liste retournée");
            }
        }

        [Test]
        public void LoadSessionsOkWithInvalidLastReadDateTime()
        {
            string filePath = Path.GetFullPath("../../../Resources/LoadSessionsOkWithInvalidLastReadDateTime/d170051-session.json");

            try
            {
                JsonSaveSessionRepository repository = new(filePath);
                IList<ICanCreateUserReadSession> collectedSessions = repository.LoadUserReadSessions().ToList();

                Assert.That(collectedSessions, Is.Not.Null);
                Assert.That(collectedSessions, Has.Count.EqualTo(1));

                var collectedSession = collectedSessions[0];

                Assert.Multiple(() =>
                {
                    Assert.That(collectedSession.CurrentBookIsbn, Is.EqualTo("2-170051-01-5"));
                    Assert.That(collectedSession.CurrentPageNumber, Is.EqualTo(21));
                    Assert.That(collectedSession.FirstRead, Is.EqualTo(DateTime.Parse("2015-06-25T23:48:00.0000000")));
                    Assert.That(collectedSession.LastRead, Is.EqualTo(DateTime.Parse("2015-06-30T11:15:23.0000000")));
                });
            }
            catch (Exception)
            {
                Assert.Fail("La méthode LoadUserReadSessions ne doit pas déclencher d'exception lorsqu'une session dans le fichier contient un datetime invalide pour la dernière lecture du livre. Cette session n'est tout simplement pas reprise dans la liste retournée");
            }
        }

        [Test]
        public void LoadSessionsOkWithFileDoesNotExist()
        {
            string filePath = Path.GetFullPath("../../../Resources/LoadSessionsOkWithFileDoesNotExist/d170051-session.json");

            try
            {
                JsonSaveSessionRepository repository = new(filePath);
                IList<ICanCreateUserReadSession> collectedSessions = repository.LoadUserReadSessions().ToList();

                Assert.That(collectedSessions, Is.Not.Null);
                Assert.That(collectedSessions, Has.Count.EqualTo(0));
            }
            catch (Exception)
            {
                Assert.Fail("La méthode LoadUserReadSessions ne devrait pas déclencher d'exception lorsque le fichier contenant les sessions de lecture n'existe pas (à la première utilisation de l'application, celui-ci n'existe pas). Une liste vide devrait être retournée");
            }
        }

        [Test]
        public void LoadSessionsOkWithExtraFields()
        {
            string filePath = Path.GetFullPath("../../../Resources/LoadSessionsOkWithExtraFields/d170051-session.json");

            try
            {
                JsonSaveSessionRepository repository = new(filePath);
                IList<ICanCreateUserReadSession> collectedSessions = repository.LoadUserReadSessions().ToList();

                Assert.That(collectedSessions, Is.Not.Null);
                Assert.That(collectedSessions, Has.Count.EqualTo(1));

                var collectedSession = collectedSessions[0];

                Assert.Multiple(() =>
                {
                    Assert.That(collectedSession.CurrentBookIsbn, Is.EqualTo("2-170051-01-5"));
                    Assert.That(collectedSession.CurrentPageNumber, Is.EqualTo(21));
                    Assert.That(collectedSession.FirstRead, Is.EqualTo(DateTime.Parse("2015-06-25T23:48:00.0000000")));
                    Assert.That(collectedSession.LastRead, Is.EqualTo(DateTime.Parse("2015-06-30T11:15:23.0000000")));
                });
            }
            catch (Exception)
            {
                Assert.Fail("La méthode LoadUserReadSessions ne devrait pas déclencher d'exception quand une session dans le fichier contient un ou plusieurs champs en trop");
            }
        }

        [Test]
        public void LoadSessionsWithEmptyFile()
        {
            string filePath = Path.GetFullPath("../../../Resources/LoadSessionsWithEmptyFile/d170051-session.json");

            try
            {
                JsonSaveSessionRepository repository = new(filePath);
                IList<ICanCreateUserReadSession> collectedSessions = repository.LoadUserReadSessions().ToList();
                Assert.Fail("La méthode LoadUserReadSessions devrait déclencher une exception car le fichier JSON est vide");
            }
            catch (Exception e)
            {
                Assert.That(e.Message, Is.EqualTo("Veuillez fournir une liste correcte de sessions de lecture (fichier vide ou non cohérent)"));
            }
        }

        [Test]
        public void SaveCorrectListOfSessions()
        {
            // Dans ce test, on sauvegarde une liste correcte de sessions de lecture dans un fichier et un dossier qui existe déjà
            string filePath = Path.GetFullPath("../../../Resources/SaveSessionsOkWithFileAndFolderExist/d170051-session.json");

            try
            {
                JsonSaveSessionRepository repository = new(filePath);
                IList<ICanCreateUserReadSession> sessionsToSave = new List<ICanCreateUserReadSession>()
                {
                    new UserReadSession(_firstBookIsbn,_firstBookCurrentPage,_firstBookFirstRead,_firstBookLastRead),
                    new UserReadSession(_secondBookIsbn,_secondBookCurrentPage,_secondBookFirstRead,_secondBookLastRead),
                    new UserReadSession(_thirdBookIsbn,_thirdBookCurrentPage,_thirdBookFirstRead,_thirdBookLastRead)
                };
                Assert.Multiple(() =>
                {
                    Assert.That(File.Exists(filePath), Is.True);
                    Assert.That(new FileInfo(filePath).Length, Is.EqualTo(_utf8Bom));
                });

                repository.SaveUserReadSessions(sessionsToSave);

                Assert.That(new FileInfo(filePath).Length, Is.GreaterThan(_utf8Bom));

                IList<ICanCreateUserReadSession> collectedSessions = repository.LoadUserReadSessions().ToList();

                Assert.That(collectedSessions, Has.Count.EqualTo(sessionsToSave.Count));

                for (int i = 0; i < collectedSessions.Count; i++)
                {
                    var sessionToSave = sessionsToSave[i];
                    var sessionSaved = collectedSessions[i];

                    Assert.Multiple(() =>
                    {
                        Assert.That(sessionToSave.CurrentBookIsbn, Is.EqualTo(sessionSaved.CurrentBookIsbn));
                        Assert.That(sessionToSave.CurrentPageNumber, Is.EqualTo(sessionSaved.CurrentPageNumber));
                        Assert.That(sessionToSave.FirstRead.ToString(), Is.EqualTo(sessionSaved.FirstRead.ToString()));
                        Assert.That(sessionToSave.LastRead.ToString(), Is.EqualTo(sessionSaved.LastRead.ToString()));
                    });
                }
            }
            catch (Exception)
            {
                Assert.Fail("La méthode SaveUserReadSessions ne devrait pas déclencher d'exception car la liste de sessions de lecture à sauvegarder est correcte et le fichier existe et est accessible");
            }
            finally
            {
                CleanFile(filePath);
            }
        }

        [Test]
        public void SaveSessionsWithFileAndFolderDoNotExist()
        {
#pragma warning disable CS8600
            // Ce test prouve que le dossier et le fichier sont créés grâce à la méthode SaveUserReadSessions s'ils n'existent pas
            string filePath = Path.GetFullPath("../../../Resources/SaveSessionsOkWithFileAndFolderDoNotExist/d170051-session.json");
            string folderPath = Path.GetDirectoryName(filePath);

            try
            {
                JsonSaveSessionRepository repository = new(filePath);
                IList<ICanCreateUserReadSession> sessionsToSave = new List<ICanCreateUserReadSession>()
                {
                    new UserReadSession(_firstBookIsbn,_firstBookCurrentPage,_firstBookFirstRead,_firstBookLastRead),
                    new UserReadSession(_secondBookIsbn,_secondBookCurrentPage,_secondBookFirstRead,_secondBookLastRead),
                    new UserReadSession(_thirdBookIsbn,_thirdBookCurrentPage,_thirdBookFirstRead,_thirdBookLastRead)
                };
                Assert.Multiple(() =>
                {
                    Assert.That(File.Exists(filePath), Is.False);
                    Assert.That(Directory.Exists(folderPath), Is.False);
                });

                repository.SaveUserReadSessions(sessionsToSave);

                Assert.That(File.Exists(filePath), Is.True);
                Assert.That(Directory.Exists(folderPath), Is.True);
                Assert.That(new FileInfo(filePath).Length, Is.GreaterThan(_utf8Bom));

                IList<ICanCreateUserReadSession> collectedSessions = repository.LoadUserReadSessions().ToList();

                Assert.That(collectedSessions, Has.Count.EqualTo(sessionsToSave.Count));

                for (int i = 0; i < collectedSessions.Count; i++)
                {
                    var sessionToSave = sessionsToSave[i];
                    var sessionSaved = collectedSessions[i];

                    Assert.Multiple(() =>
                    {
                        Assert.That(sessionToSave.CurrentBookIsbn, Is.EqualTo(sessionSaved.CurrentBookIsbn));
                        Assert.That(sessionToSave.CurrentPageNumber, Is.EqualTo(sessionSaved.CurrentPageNumber));
                        Assert.That(sessionToSave.FirstRead.ToString(), Is.EqualTo(sessionSaved.FirstRead.ToString()));
                        Assert.That(sessionToSave.LastRead.ToString(), Is.EqualTo(sessionSaved.LastRead.ToString()));
                    });
                }
            }
            catch (Exception)
            {
                Assert.Fail("La méthode SaveUserReadSessions ne devrait pas déclencher d'exception car la liste de sessions de lecture est correcte et le fichier et dossier seront créés par cette méthode");
            }
            finally
            {
                DeleteFileAndFolder(filePath);
            }
        }

        [Test]
        public void SaveSessionsWithNullListOfSessions()
        {
#pragma warning disable 8625
            string filePath = Path.GetFullPath("../../../Resources/SaveSessionsNotOkWithNullListOfSessions/d170051-session.json");

            try
            {
                JsonSaveSessionRepository repository = new(filePath);

                repository.SaveUserReadSessions(null);

                Assert.Fail("La méthode SaveUserReadSessions doit déclencher une exception lorsque la liste de sessions de lecture à sauvegarder est nulle");
            }
            catch (Exception e)
            {
                Assert.That(File.Exists(filePath), Is.False);
                Assert.That(e.Message, Is.EqualTo("Veuillez fournir une liste de sessions de lecture correcte"));
            }
            finally
            {
                DeleteFileAndFolder(filePath);
            }
        }

        [Test]
        public void SaveSessionsWithOnlyNullSessionsInList()
        {
            string filePath = Path.GetFullPath("../../../Resources/SaveSessionsOkWithOnlyNullSessionsInList/d170051-session.json");

            try
            {
                JsonSaveSessionRepository repository = new(filePath);
                IList<ICanCreateUserReadSession> sessionsToSave = new List<ICanCreateUserReadSession>()
                {
                    null,
                    null
                };

                Assert.That(File.Exists(filePath), Is.False);

                repository.SaveUserReadSessions(sessionsToSave);

                Assert.That(File.Exists(filePath), Is.True);

                IList<ICanCreateUserReadSession> collectedSessions = repository.LoadUserReadSessions().ToList();

                Assert.That(collectedSessions, Has.Count.EqualTo(0));
            }
            catch (Exception)
            {
                Assert.Fail("La méthode SaveUserReadSessions ne devrait pas déclencher d'exception lorsque que la liste de sessions à sauvegarder n'est pas nulle et contient uniquement des sessions nulles. Ces sessions ne doivent juste pas être sauvegardées et donc reprises dans le fichier");
            }
            finally
            {
                DeleteFileAndFolder(filePath);
            }
        }

        [Test]
        public void SaveSessionsWithNullSessionAndCorrectSession()
        {
            string filePath = Path.GetFullPath("../../../Resources/SaveSessionsOkWithNullSessionAndCorrectSession/d170051-session.json");

            try
            {
                JsonSaveSessionRepository repository = new(filePath);

                IList<ICanCreateUserReadSession> sessionsToSave = new List<ICanCreateUserReadSession>()
                {
                    null,
                    new UserReadSession(_firstBookIsbn,_firstBookCurrentPage,_firstBookFirstRead,_firstBookLastRead)
                };

                Assert.That(File.Exists(filePath), Is.False);

                repository.SaveUserReadSessions(sessionsToSave);

                Assert.That(File.Exists(filePath), Is.True);

                IList<ICanCreateUserReadSession> collectedSessions = repository.LoadUserReadSessions().ToList();

                Assert.That(collectedSessions, Has.Count.EqualTo(1));

                var sessionSaved = collectedSessions[0];
                var sessionToSave = sessionsToSave[1];

                Assert.Multiple(() =>
                {
                    Assert.That(sessionToSave.CurrentBookIsbn, Is.EqualTo(sessionSaved.CurrentBookIsbn));
                    Assert.That(sessionToSave.CurrentPageNumber, Is.EqualTo(sessionSaved.CurrentPageNumber));
                    Assert.That(sessionToSave.FirstRead.ToString(), Is.EqualTo(sessionSaved.FirstRead.ToString()));
                    Assert.That(sessionToSave.LastRead.ToString(), Is.EqualTo(sessionSaved.LastRead.ToString()));
                });
            }
            catch (Exception)
            {
                Assert.Fail("La méthode SaveUserReadSessions ne devrait pas déclencher d'exception lorsqu'on souhaite sauvegarder une liste de sessions de lecture qui contient des sessions correctes et nulles. Les sessions nulles ne seront pas reprises dans le fichier");
            }
            finally
            {
                DeleteFileAndFolder(filePath);
            }
        }


        private void DeleteFileAndFolder(string filePath)
        {
            if (File.Exists(filePath))
            {
                try
                {
                    var directoryPath = Path.GetDirectoryName(filePath);
                    if (Directory.Exists(directoryPath))
                    {
                        Directory.Delete(directoryPath, true);
                    }
                }
                catch (Exception)
                {
                    Assert.Fail("Erreur durant la suppression du fichier et dossier de test");
                }
            }
        }

        private void CleanFile(string filePath)
        {
            if (File.Exists(filePath))
            {
                try
                {
                    using TextWriter writer = new StreamWriter(new FileStream(filePath, FileMode.Create), Encoding.UTF8);
                }
                catch (Exception)
                {
                    Assert.Fail("Erreur durant la suppression des éléments du fichier");
                }
            }
        }

    }
}
