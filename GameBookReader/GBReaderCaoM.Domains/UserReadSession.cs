namespace GBReaderCaoM.Domains
{
    public class UserReadSession : ICanCreateUserReadSession
    {
        private readonly string _currentBookIsbn;
        private int _currentPageNumber;
        private readonly DateTime _firstRead;
        private DateTime _lastRead;

        public UserReadSession(string currentBookIsbn, int currentPageNumber)
        {
            ValidateIsbn(currentBookIsbn);
            this._currentBookIsbn = currentBookIsbn;
            this._currentPageNumber = Math.Max(currentPageNumber, 2);

            DateTime currentDateTime = DateTime.Now;
            this._firstRead = currentDateTime;
            this._lastRead = currentDateTime;
        }

        public UserReadSession(string currentBookIsbn, int currentPageNumber, DateTime firstRead, DateTime lastRead)
        {
            ValidateIsbn(currentBookIsbn);

            this._currentBookIsbn = currentBookIsbn;
            this._currentPageNumber = Math.Max(currentPageNumber, 2);
            this._firstRead = firstRead;
            this._lastRead = lastRead;
        }

        private void ValidateIsbn(string isbn) => _ = new ISBN(isbn);

        public string CurrentBookIsbn => _currentBookIsbn;

        public int CurrentPageNumber
        {
            get => _currentPageNumber;
            set => _currentPageNumber = Math.Max(value, 2);
        }

        public DateTime FirstRead => _firstRead;

        public DateTime LastRead => _lastRead;

        public void UpdateUserSession(int newPageNumber)
        {
            _currentPageNumber = Math.Max(newPageNumber, 2);
            _lastRead = DateTime.Now;
        }

    }
}
