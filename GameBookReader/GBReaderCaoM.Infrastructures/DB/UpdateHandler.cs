using GBReaderCaoM.Domains;

namespace GBReaderCaoM.Infrastructures.DB
{
    public class UpdateHandler
    {
        private readonly IDictionary<string, int> _booksMap = new Dictionary<string, int>();
        private readonly IDictionary<int, ICanCreateBookPage> _pages = new Dictionary<int, ICanCreateBookPage>();

        public void AddBookInList(int idBook, string isbn)
        {
            if (idBook > 0 && !string.IsNullOrEmpty(isbn) && isbn.Length == 13)
            {
                _booksMap[isbn] = idBook;
            }
        }

        public int GetIdBookWithIsbn(string isbn)
        {
            if (_booksMap.ContainsKey(isbn))
            {
                return _booksMap[isbn];
            }
            return -1;
        }

        public void ClearBooksMap() => _booksMap.Clear();

        public void AddPageInList(int idPage, ICanCreateBookPage page)
        {
            if (page != null && idPage > 0)
            {
                _pages[idPage] = page;
            }
        }

        public IDictionary<int, ICanCreateBookPage> PagesMap => _pages;

        public IList<ICanCreateBookPage> Pages => _pages.Values.ToList();

        public ICanCreateBookPage? GetPageWithId(int idPage)
        {
            if (_pages.ContainsKey(idPage))
            {
                return _pages[idPage];
            }
            return null;
        }

        public void ClearPagesMap() => _pages.Clear();
    }
}
