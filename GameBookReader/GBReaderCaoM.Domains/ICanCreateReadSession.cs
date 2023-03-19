namespace GBReaderCaoM.Domains
{
    public interface ICanCreateReadSession
    {
        IEnumerable<ICanCreateGameBook> Books { get; }
        bool AddBookInSession(string authorName, string isbn, string title, string resume);

        IEnumerable<ICanCreateGameBook> GetFiltredBooks(string filter);

        ICanCreateGameBook? CurrentBook { get; }

        IEnumerable<ICanCreateUserReadSession> UserReadSessions { get; }

        void UpdateCurrentBook(string isbn);

        void ClearBooksList();

        void ClearSessionsList();

        void AddUserReadSession(int newPageNumber);

        void AddUserReadSession(ICanCreateUserReadSession userReadSession);

        void DeleteUserReadSession();

        int GetCurrentPageUserSession();

        ICanCreateGameBook? GetABook(string isbn);
    }
}
