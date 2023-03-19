namespace GBReaderCaoM.Domains
{
    public interface ICanCreateUserReadSession
    {
        string CurrentBookIsbn { get; }

        int CurrentPageNumber { get; set; }

        DateTime FirstRead { get; }

        DateTime LastRead { get; }

        void UpdateUserSession(int newPageNumber);
    }
}
