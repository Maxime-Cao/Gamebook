namespace GBReaderCaoM.Infrastructures.Dtos
{
    public record UserReadSessionDto
    {
        private readonly string _currentBookIsbn;
        private readonly int _currentPageNumber;
        private readonly string _firstRead;
        private readonly string _lastRead;

        public UserReadSessionDto(string currentBookIsbn, int currentPageNumber, string firstRead, string lastRead)
        {
            _currentBookIsbn = currentBookIsbn;
            _currentPageNumber = currentPageNumber;
            _firstRead = firstRead;
            _lastRead = lastRead;
        }

        public string CurrentBookIsbn => _currentBookIsbn;

        public int CurrentPageNumber => _currentPageNumber;

        public string FirstRead => _firstRead;

        public string LastRead => _lastRead;
    }
}
