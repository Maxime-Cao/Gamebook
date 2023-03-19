using GBReaderCaoM.Domains;
using GBReaderCaoM.Infrastructures.Dtos;

namespace GBReaderCaoM.Infrastructures.Mapper
{
    public class Mapping
    {
        public IEnumerable<ICanCreateUserReadSession> FromDto(ReadSessionDto? readSessionDto)
        {
            if (readSessionDto == null || readSessionDto.Sessions == null)
            {
                throw new ArgumentException("Veuillez fournir une liste correcte de sessions de lecture (fichier vide ou non cohérent)");
            }
            IList<ICanCreateUserReadSession> newUserReadSessions = new List<ICanCreateUserReadSession>();
            foreach (var userReadSessionDto in readSessionDto.Sessions)
            {
                if (userReadSessionDto != null)
                {
                    try
                    {
                        AddNewUserReadSession(userReadSessionDto, newUserReadSessions);
                    }
                    catch (Exception ex) when (ex is ArgumentException || ex is FormatException)
                    {
                        // Ne rien faire, la session utilisateur de lecture ne sera pas prise en compte
                    }
                }
            }

            return newUserReadSessions;
        }

        public ReadSessionDto ToDto(IEnumerable<ICanCreateUserReadSession> userReadSessions)
        {
            if (userReadSessions == null)
            {
                throw new ArgumentException("Veuillez fournir une liste de sessions de lecture correcte");
            }
            IList<UserReadSessionDto> userReadSessionDtos = new List<UserReadSessionDto>();

            foreach (var userReadSession in userReadSessions)
            {
                if (userReadSession != null)
                {
                    userReadSessionDtos.Add(new UserReadSessionDto(userReadSession.CurrentBookIsbn, userReadSession.CurrentPageNumber, userReadSession.FirstRead.ToString("O"), userReadSession.LastRead.ToString("O")));
                }
            }

            return new ReadSessionDto(userReadSessionDtos);
        }

        private void AddNewUserReadSession(UserReadSessionDto userReadSessionDto, IList<ICanCreateUserReadSession> newUserReadSessions)
        {
            ICanCreateUserReadSession newUserReadSession = new UserReadSession(userReadSessionDto.CurrentBookIsbn, userReadSessionDto.CurrentPageNumber, DateTime.Parse(userReadSessionDto.FirstRead), DateTime.Parse(userReadSessionDto.LastRead));
            newUserReadSessions.Add(newUserReadSession);
        }
    }
}
