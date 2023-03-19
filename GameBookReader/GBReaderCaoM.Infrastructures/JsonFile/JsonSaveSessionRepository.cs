using GBReaderCaoM.Domains;
using GBReaderCaoM.Infrastructures.Dtos;
using GBReaderCaoM.Infrastructures.Mapper;
using GBReaderCaoM.Repositories;
using GBReaderCaoM.Repositories.Exceptions;
using Newtonsoft.Json;
using System.Text;

namespace GBReaderCaoM.Infrastructures.JsonFile
{
    public class JsonSaveSessionRepository : IUserReadSessionRepository
    {
        private readonly string _filePath;

        public JsonSaveSessionRepository()
        {
            // Source pour la méthode Environment.GetFolderPath(...) : https://stackoverflow.com/questions/1140383/how-can-i-get-the-current-user-directory
            _filePath = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.UserProfile), "UE36", "d170051-session.json");
        }

        public JsonSaveSessionRepository(string? filePath)
        {
            // Source pour la méthode GetFullPath : https://stackoverflow.com/questions/4796254/relative-path-to-absolute-path-in-c
            filePath = filePath == null ? null : Path.GetFullPath(filePath);
            if (filePath != null && filePath.EndsWith(".json"))
            {
                _filePath = filePath;
            }
            else
            {
                throw new ArgumentException("Le chemin du fichier ne doit pas être nul et doit correspondre à un fichier .json");
            }
        }

        public IEnumerable<ICanCreateUserReadSession> LoadUserReadSessions()
        {
            Mapping map = new();
            IEnumerable<ICanCreateUserReadSession> userReadSessions = new List<ICanCreateUserReadSession>();

            if (File.Exists(_filePath))
            {
                try
                {
                    using TextReader reader = new StreamReader(new FileStream(_filePath, FileMode.Open), Encoding.UTF8);
                    JsonSerializer serializer = new();
                    var readSessionDto = serializer.Deserialize(reader, typeof(ReadSessionDto)) as ReadSessionDto;
                    userReadSessions = map.FromDto(readSessionDto);
                }
                catch (ArgumentException)
                {
                    throw new ArgumentException("Veuillez fournir une liste correcte de sessions de lecture (fichier vide ou non cohérent)");
                }
                catch (Exception ex) when (ex is JsonSerializationException || ex is JsonReaderException)
                {
                    throw new LoadUserReadSessionException("Erreur durant la lecture du fichier JSON. Veuillez vérifier la syntaxe des éléments du fichier.", ex);
                }
                catch (IOException ex)
                {
                    throw new LoadUserReadSessionException("Erreur durant la lecture du fichier JSON", ex);
                }
            }

            return userReadSessions;
        }

        public void SaveUserReadSessions(IEnumerable<ICanCreateUserReadSession> userReadSessions)
        {
            Mapping map = new();
            ReadSessionDto readSessionDto = map.ToDto(userReadSessions);

            CreateDirectoryForSaving();

            try
            {
                using TextWriter writer = new StreamWriter(new FileStream(_filePath, FileMode.Create), Encoding.UTF8);
                JsonSerializer serializer = new();
                serializer.Serialize(writer, readSessionDto);
            }
            catch (Exception ex) when (ex is JsonException || ex is IOException)
            {
                throw new SaveUserReadSessionException("Erreur durant la sauvegarde des sessions de lecture", ex);
            }
        }

        private void CreateDirectoryForSaving()
        {
            // Source pour la création du dossier : https://stackoverflow.com/questions/3695163/filestream-and-creating-folders
            try
            {
                string? folderPath = Path.GetDirectoryName(_filePath);
                if (folderPath is not null && !Directory.Exists(folderPath))
                {
                    Directory.CreateDirectory(folderPath);
                }
            }
            catch (Exception e)
            {
                throw new CreateDirectoryException("Erreur durant la création du dossier de sauvegarde des sessions de lecture", e);
            }
        }
    }

}
