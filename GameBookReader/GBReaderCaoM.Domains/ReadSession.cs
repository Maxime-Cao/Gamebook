namespace GBReaderCaoM.Domains
{
    public class ReadSession : ICanCreateReadSession
    {
        private readonly IList<ICanCreateGameBook> _books = new List<ICanCreateGameBook>();
        private readonly IList<ICanCreateUserReadSession> _sessions = new List<ICanCreateUserReadSession>();
        private ICanCreateGameBook? _currentBook;

        public IEnumerable<ICanCreateGameBook> Books
        {
            get
            {
                IList<ICanCreateGameBook> copyBooks = new List<ICanCreateGameBook>();
                foreach (ICanCreateGameBook book in _books)
                {
                    if (book != null)
                    {
                        copyBooks.Add(new GameBookReader(book));
                    }
                }
                return copyBooks;
            }

        }
        private bool CheckIfBookExistsInSession(ICanCreateGameBook book)
        {
            foreach (ICanCreateGameBook canCreateBook in _books)
            {
                if (canCreateBook.Equals(book))
                {
                    throw new ArgumentException("Ce livre existe déjà (ISBN existant)");
                }
            }
            return false;
        }

        public bool AddBookInSession(string authorName, string isbn, string title, string resume)
        {
            try
            {
                ICanCreateGameBook gamebook = new GameBookReader(authorName, isbn, title, resume);
                if (!CheckIfBookExistsInSession(gamebook))
                {
                    _books.Add(gamebook);
                    SortBooksList(_books);
                    return true;
                }
                return false;
            }
            catch (ArgumentException ex)
            {
                throw ex;
            }
        }

        private static void SortBooksList(IList<ICanCreateGameBook> booksList) => booksList.OrderBy(b => b.IsbnRepresentation).ToList();

        private IList<ICanCreateGameBook> SearchBooksWithIsbnOrTitle(string filter)
        {
            IList<ICanCreateGameBook> booksFound = new List<ICanCreateGameBook>();
            filter = filter.ToLower();

            foreach (var book in _books)
            {
                string currentIsbn = book.IsbnRepresentation.ToLower();
                string currentTitle = book.TitleBook.ToLower();
                if (currentIsbn.Equals(filter) || currentTitle.Contains(filter))
                {
                    booksFound.Add(new GameBookReader(book));
                }
            }

            SortBooksList(booksFound);

            return booksFound;
        }

        public IEnumerable<ICanCreateGameBook> GetFiltredBooks(string filter)
        {
            if (string.IsNullOrWhiteSpace(filter))
            {
                return this.Books;
            }

            filter = filter.Trim();

            return SearchBooksWithIsbnOrTitle(filter);
        }

        public ICanCreateGameBook? CurrentBook => _currentBook;

        public IEnumerable<ICanCreateUserReadSession> UserReadSessions => _sessions;

        public void UpdateCurrentBook(string isbn)
        {
            foreach (var book in _books)
            {
                if (book.IsbnRepresentation.Equals(isbn))
                {
                    _currentBook = book;
                    break;
                }
            }

        }

        public void ClearBooksList() => _books.Clear();

        public void ClearSessionsList() => _sessions.Clear();

        public void AddUserReadSession(int newPageNumber)
        {
            var currentBookIsbn = _currentBook?.IsbnRepresentation;

            if (currentBookIsbn != null)
            {
                var userSessionFound = GetUserReadSession(currentBookIsbn);

                if (userSessionFound == null)
                {
                    _sessions.Add(new UserReadSession(currentBookIsbn, newPageNumber));
                }
                else
                {
                    userSessionFound.UpdateUserSession(newPageNumber);
                }
            }
        }

        public void AddUserReadSession(ICanCreateUserReadSession userReadSession)
        {
            if (userReadSession != null)
            {
                _sessions.Add(new UserReadSession(userReadSession.CurrentBookIsbn, userReadSession.CurrentPageNumber, userReadSession.FirstRead, userReadSession.LastRead));
            }
        }

        public void DeleteUserReadSession()
        {
            string? bookIsbn = _currentBook?.IsbnRepresentation;
            foreach (var userSession in _sessions)
            {
                if (userSession.CurrentBookIsbn.Equals(bookIsbn))
                {
                    _sessions.Remove(userSession);
                    break;
                }
            }
        }

        private ICanCreateUserReadSession? GetUserReadSession(string bookIsbn)
        {
            foreach (var userSession in _sessions)
            {
                if (userSession.CurrentBookIsbn.Equals(bookIsbn))
                {
                    return userSession;
                }
            }
            return null;
        }

        public int GetCurrentPageUserSession()
        {
            foreach (var userSession in _sessions)
            {
                if (userSession.CurrentBookIsbn.Equals(_currentBook?.IsbnRepresentation))
                {
                    return userSession.CurrentPageNumber;
                }
            }
            return 1;
        }

        public ICanCreateGameBook? GetABook(string isbn)
        {
            foreach (var book in _books)
            {
                if (book.IsbnRepresentation.Equals(isbn))
                {
                    return book;
                }
            }
            return null;
        }
    }
}
