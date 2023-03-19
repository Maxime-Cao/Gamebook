using GBReaderCaoM.Domains;
using GBReaderCaoM.Infrastructures.Dtos;
using GBReaderCaoM.Infrastructures.Mapper;
using NUnit.Framework;

namespace GBReaderCaoM.Infrastructures.Tests.Mapper
{
#pragma warning disable CS8625, CS8604
    public class MappingTest
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

        [Test]
        public void FromDtoWithCorrectListOfUserReadSessionDto()
        {
            Mapping map = new();

            IList<UserReadSessionDto> sessionsListDto = new List<UserReadSessionDto>()
            {
                new(_firstBookIsbn,_firstBookCurrentPage,_firstBookFirstRead.ToString("O"),_firstBookLastRead.ToString("O")),
                new(_secondBookIsbn,_secondBookCurrentPage,_secondBookFirstRead.ToString("O"),_secondBookLastRead.ToString("O")),
                new(_thirdBookIsbn,_thirdBookCurrentPage,_thirdBookFirstRead.ToString("O"),_thirdBookLastRead.ToString("O"))
            };

            ReadSessionDto readSessionDto = new(sessionsListDto);

            var userReadSessionDtos = readSessionDto.Sessions;

            try
            {
                IList<ICanCreateUserReadSession> userReadSessions = map.FromDto(readSessionDto).ToList();
                Assert.That(userReadSessions, Is.Not.Null);
                Assert.That(userReadSessions, Has.Count.EqualTo(userReadSessionDtos!.Count));

                for (int i = 0; i < userReadSessions.Count; i++)
                {
                    var currentUserSessionDto = userReadSessionDtos![i];
                    var currentUserSession = userReadSessions[i];
                    Assert.Multiple(() =>
                    {
                        Assert.That(currentUserSessionDto.CurrentBookIsbn, Is.EqualTo(currentUserSession.CurrentBookIsbn));
                        Assert.That(currentUserSessionDto.CurrentPageNumber, Is.EqualTo(currentUserSession.CurrentPageNumber));
                        Assert.That(currentUserSessionDto.FirstRead, Is.EqualTo(currentUserSession.FirstRead.ToString("O")));
                        Assert.That(currentUserSessionDto.LastRead, Is.EqualTo(currentUserSession.LastRead.ToString("O")));
                    });
                }
            }
            catch (Exception)
            {
                Assert.Fail("La méthode FromDto ne doit pas déclencher d'exception car la liste de sessions n'est pas nulle et contient uniquement des sessions correctement formattées");
            }

        }

        [Test]
        public void FromDtoWithOneValidUserReadSessionAndOneInvalid()
        {
            Mapping map = new();

            IList<UserReadSessionDto> sessionsListDto = new List<UserReadSessionDto>()
            {
                new(_firstBookIsbn,_firstBookCurrentPage,_firstBookFirstRead.ToString("O"),_firstBookLastRead.ToString("O")),
                null
            };

            ReadSessionDto readSessionDto = new(sessionsListDto);

            var userReadSessionDtos = readSessionDto.Sessions;

            try
            {
                IList<ICanCreateUserReadSession> userReadSessions = map.FromDto(readSessionDto).ToList();
                Assert.That(userReadSessions, Is.Not.Null);
                Assert.That(userReadSessions, Has.Count.EqualTo(1));

                var validUserReadSessionDto = userReadSessionDtos![0];
                var validUserReadSession = userReadSessions[0];
                Assert.Multiple(() =>
                {
                    Assert.That(validUserReadSessionDto.CurrentBookIsbn, Is.EqualTo(validUserReadSession.CurrentBookIsbn));
                    Assert.That(validUserReadSessionDto.CurrentPageNumber, Is.EqualTo(validUserReadSession.CurrentPageNumber));
                    Assert.That(validUserReadSessionDto.FirstRead, Is.EqualTo(validUserReadSession.FirstRead.ToString("O")));
                    Assert.That(validUserReadSessionDto.LastRead, Is.EqualTo(validUserReadSession.LastRead.ToString("O")));
                });
            }
            catch (Exception)
            {
                Assert.Fail("La méthode FromDto ne doit pas déclencher d'exception car la liste de sessions n'est pas nulle et contient une session correctement formattée");
            }
        }

        [Test]
        public void FromDtoWithNullReadSessionDto()
        {
            Mapping map = new();
            ReadSessionDto? readSessionDto = null;

            try
            {
                IList<ICanCreateUserReadSession> userReadSessions = map.FromDto(readSessionDto).ToList();
                Assert.Fail("La méthode FromDto devrait déclencher une exception lorsque l'objet ReadSessionDto en argument est 'null'");
            }
            catch (Exception e)
            {
                Assert.That(e.Message, Is.EqualTo("Veuillez fournir une liste correcte de sessions de lecture (fichier vide ou non cohérent)"));
            }
        }

        [Test]
        public void FromDtoWithNullListOfUserReadSessionDtos()
        {
            Mapping map = new();
            ReadSessionDto readSessionDto = new(null);

            try
            {
                IList<ICanCreateUserReadSession> userReadSessions = map.FromDto(readSessionDto).ToList();
                Assert.Fail("La méthode FromDto devrait déclencher une exception lorsque l'objet ReadSessionDto en argument contient une référence 'null' pour la liste de sessions");
            }
            catch (Exception e)
            {
                Assert.That(e.Message, Is.EqualTo("Veuillez fournir une liste correcte de sessions de lecture (fichier vide ou non cohérent)"));
            }

        }

        [Test]
        public void FromDtoWithInvalidIsbn()
        {
            Mapping map = new();
            IList<UserReadSessionDto> sessionsListDto = new List<UserReadSessionDto>()
            {
                new("2-1344-2-X",_firstBookCurrentPage,_firstBookFirstRead.ToString("O"),_firstBookLastRead.ToString("O"))
            };

            ReadSessionDto readSessionDto = new(sessionsListDto);
            Assert.That(readSessionDto!.Sessions, Has.Count.EqualTo(1));

            try
            {
                IList<ICanCreateUserReadSession> userReadSessions = map.FromDto(readSessionDto).ToList();
                Assert.That(userReadSessions, Is.Not.Null);
                Assert.That(userReadSessions, Is.Empty);
            }
            catch (Exception)
            {
                Assert.Fail("La méthode FromDto ne doit pas déclencher d'exception lorsqu'une session dans la liste Dto contient un isbn invalide. Cette session n'est tout simplement pas reprise dans la liste retournée");
            }

        }


        [Test]
        public void FromDtoWithInvalidNumberPage()
        {
            Mapping map = new();
            IList<UserReadSessionDto> sessionsListDto = new List<UserReadSessionDto>()
            {
                new(_firstBookIsbn,-5,_firstBookFirstRead.ToString("O"),_firstBookLastRead.ToString("O"))
            };

            ReadSessionDto readSessionDto = new(sessionsListDto);
            Assert.That(readSessionDto!.Sessions, Has.Count.EqualTo(1));

            try
            {
                IList<ICanCreateUserReadSession> userReadSessions = map.FromDto(readSessionDto).ToList();
                Assert.That(userReadSessions, Is.Not.Null);
                Assert.That(userReadSessions, Has.Count.EqualTo(1));
                Assert.That(userReadSessions[0].CurrentPageNumber, Is.EqualTo(2));
            }
            catch (Exception)
            {
                Assert.Fail("La méthode FromDto ne doit pas déclencher d'exception lorsqu'une session dans la liste Dto contient un numéro de page invalide (plus petit que 1). Le numéro de page de cette session sera égal à 1");
            }
        }

        [Test]
        public void FromDtoWithInvalidFirstReadDateTime()
        {
            Mapping map = new();
            IList<UserReadSessionDto> sessionsListDto = new List<UserReadSessionDto>()
            {
                new(_firstBookIsbn,_firstBookCurrentPage,null,_firstBookLastRead.ToString("O"))
            };

            ReadSessionDto readSessionDto = new(sessionsListDto);
            Assert.That(readSessionDto!.Sessions, Has.Count.EqualTo(1));

            try
            {
                IList<ICanCreateUserReadSession> userReadSessions = map.FromDto(readSessionDto).ToList();
                Assert.That(userReadSessions, Is.Not.Null);
                Assert.That(userReadSessions, Is.Empty);
            }
            catch (Exception)
            {
                Assert.Fail("La méthode FromDto ne doit pas déclencher d'exception lorsqu'une session dans la liste Dto contient un datetime invalide pour la première lecture du livre. Cette session n'est tout simplement pas reprise dans la liste retournée");
            }
        }

        [Test]
        public void FromDtoWithInvalidLastReadDateTime()
        {
            Mapping map = new();
            IList<UserReadSessionDto> sessionsListDto = new List<UserReadSessionDto>()
            {
                new(_firstBookIsbn,_firstBookCurrentPage,_firstBookFirstRead.ToString("O"),null)
            };

            ReadSessionDto readSessionDto = new(sessionsListDto);
            Assert.That(readSessionDto!.Sessions, Has.Count.EqualTo(1));

            try
            {
                IList<ICanCreateUserReadSession> userReadSessions = map.FromDto(readSessionDto).ToList();
                Assert.That(userReadSessions, Is.Not.Null);
                Assert.That(userReadSessions, Is.Empty);
            }
            catch (Exception)
            {
                Assert.Fail("La méthode FromDto ne doit pas déclencher d'exception lorsqu'une session dans la liste Dto contient un datetime invalide pour la dernière lecture du livre. Cette session n'est tout simplement pas reprise dans la liste retournée");
            }
        }

        [Test]
        public void ToDtoWithCorrectListOfUserReadSessions()
        {
            Mapping map = new();
            IList<ICanCreateUserReadSession> sessionsList = new List<ICanCreateUserReadSession>()
            {
                new UserReadSession(_firstBookIsbn,_firstBookCurrentPage,_firstBookFirstRead,_firstBookLastRead),
                new UserReadSession(_secondBookIsbn,_secondBookCurrentPage,_secondBookFirstRead,_secondBookLastRead),
                new UserReadSession(_thirdBookIsbn,_thirdBookCurrentPage,_thirdBookFirstRead,_thirdBookLastRead)
            };

            try
            {
                ReadSessionDto readSessionDto = map.ToDto(sessionsList);
                Assert.That(readSessionDto, Is.Not.Null);
                var userReadSessionDtos = readSessionDto.Sessions;
                Assert.That(userReadSessionDtos, Is.Not.Null);
                Assert.That(userReadSessionDtos, Has.Count.EqualTo(sessionsList.Count));

                for (int i = 0; i < userReadSessionDtos.Count; i++)
                {
                    var currentUserSessionDto = userReadSessionDtos[i];
                    var currentUserSession = sessionsList[i];
                    Assert.Multiple(() =>
                    {
                        Assert.That(currentUserSessionDto.CurrentBookIsbn, Is.EqualTo(currentUserSession.CurrentBookIsbn));
                        Assert.That(currentUserSessionDto.CurrentPageNumber, Is.EqualTo(currentUserSession.CurrentPageNumber));
                        Assert.That(currentUserSessionDto.FirstRead, Is.EqualTo(currentUserSession.FirstRead.ToString("O")));
                        Assert.That(currentUserSessionDto.LastRead, Is.EqualTo(currentUserSession.LastRead.ToString("O")));
                    });
                }
            }
            catch (Exception)
            {
                Assert.Fail("La méthode ToDto ne doit pas déclencher d'exception car la liste de sessions n'est pas nulle et contient uniquement des sessions correctement formattées");
            }
        }

        [Test]
        public void ToDtoWithOneValidUserReadSessionAndOneInvalid()
        {
            Mapping map = new();

            IList<ICanCreateUserReadSession> sessionsList = new List<ICanCreateUserReadSession>()
            {
                new UserReadSession(_firstBookIsbn,_firstBookCurrentPage,_firstBookFirstRead,_firstBookLastRead),
                null
            };

            try
            {
                var readSessionDto = map.ToDto(sessionsList);
                Assert.That(readSessionDto, Is.Not.Null);
                var userReadSessionDtos = readSessionDto.Sessions;
                Assert.That(userReadSessionDtos, Is.Not.Null);

                Assert.That(userReadSessionDtos, Has.Count.EqualTo(1));
                var validSessionDto = userReadSessionDtos[0];
                var validUserReadSession = sessionsList[0];

                Assert.Multiple(() =>
                {
                    Assert.That(validSessionDto.CurrentBookIsbn, Is.EqualTo(validUserReadSession.CurrentBookIsbn));
                    Assert.That(validSessionDto.CurrentPageNumber, Is.EqualTo(validUserReadSession.CurrentPageNumber));
                    Assert.That(validSessionDto.FirstRead, Is.EqualTo(validUserReadSession.FirstRead.ToString("O")));
                    Assert.That(validSessionDto.LastRead, Is.EqualTo(validUserReadSession.LastRead.ToString("O")));
                });

            }
            catch (Exception)
            {
                Assert.Fail("La méthode ToDto ne doit pas déclencher d'exception car la liste de sessions n'est pas nulle et contient une session correctement formattée");
            }
        }

        [Test]
        public void ToDtoWithNoValidUserReadSession()
        {
            Mapping map = new();
            IList<ICanCreateUserReadSession> sessionsList = new List<ICanCreateUserReadSession>()
            {
                null,
                null
            };

            try
            {
                var readSessionDto = map.ToDto(sessionsList);
                Assert.That(readSessionDto, Is.Not.Null);
                var userReadSessionDtos = readSessionDto.Sessions;
                Assert.That(userReadSessionDtos, Is.Not.Null);

                Assert.That(userReadSessionDtos, Has.Count.EqualTo(0));
            }
            catch (Exception)
            {
                Assert.Fail("La méthode ToDto ne doit pas déclencher d'exception car la liste de sessions n'est pas nulle et les éléments null de la liste doivent juste ne pas être pris en compte");
            }
        }

        [Test]
        public void ToDtoWithNullListOfUserReadSessions()
        {
            Mapping map = new();
            try
            {
                map.ToDto(null);
                Assert.Fail("La méthode ToDto doit déclencher une exception car la liste de sessions est nulle");
            }
            catch (Exception e)
            {
                Assert.That(e.Message, Is.EqualTo("Veuillez fournir une liste de sessions de lecture correcte"));
            }


        }
    }
}
