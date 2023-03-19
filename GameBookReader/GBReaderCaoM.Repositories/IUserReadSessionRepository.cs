using GBReaderCaoM.Domains;

namespace GBReaderCaoM.Repositories
{
    public interface IUserReadSessionRepository
    {
        void SaveUserReadSessions(IEnumerable<ICanCreateUserReadSession> userReadSessions);
        IEnumerable<ICanCreateUserReadSession> LoadUserReadSessions();
    }
}
