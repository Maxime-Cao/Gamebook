using GBReaderCaoM.Domains;
using GBReaderCaoM.Presentations.Views;
using GBReaderCaoM.Repositories;

namespace GBReaderCaoM.Presentations
{
    public class DisplayBooksPresenter
    {
        private readonly IDisplayBooksView _view;
        private readonly ISwitchToPages _mainView;
        private readonly ICanCreateReadSession _session;
        private readonly IFactory _factory;
        private readonly IUserReadSessionRepository _userSessionsRepository;

        public DisplayBooksPresenter(IDisplayBooksView view, ICanCreateReadSession session, IFactory factory, ISwitchToPages mainView, IUserReadSessionRepository userSessionsRepository)
        {
            if (view == null || session == null || factory == null || mainView == null || userSessionsRepository == null)
            {
                throw new ArgumentException($"Votre vue, session, repository, factory et vue principale doivent être initialisés correctement");
            }
            _view = view;
            _mainView = mainView;
            _session = session;
            _factory = factory;
            _userSessionsRepository = userSessionsRepository;

            SubscribeToEvents();
        }

        private void SubscribeToEvents()
        {
            _view.LoadBooksView += BuildBooksView;
            _view.FilterBooks += FilterListBooks;
            _view.BookSelected += UpdateBookDetailView;
            _view.StartReading += OnStartReading;
            _view.GoToStats += OnGoToStats;
        }

        private void OnGoToStats(object? sender, EventArgs args) => _mainView.SwitchToPage("DisplayStatsView");
        private void BuildBooksView(object? sender, EventArgs args)
        {
            if (LoadBooks())
            {
                LoadUserSessions();
                AddBooksInBooksView(_session.Books.ToList());
            }
        }

        private void LoadUserSessions()
        {
            try
            {
                var userSessions = _userSessionsRepository.LoadUserReadSessions();
                _session.ClearSessionsList();

                foreach (var userSession in userSessions)
                {
                    AddUserReadSessionInSession(userSession);
                }
            }
            catch (Exception e)
            {
                _view.DisplayError(e.Message);
            }
        }

        private void AddUserReadSessionInSession(ICanCreateUserReadSession userReadSession)
        {
            var targetBook = _session.GetABook(userReadSession.CurrentBookIsbn);
            if (targetBook is not null)
            {
                _session.AddUserReadSession(userReadSession);
            }
        }

        private void AddBooksInBooksView(IList<ICanCreateGameBook> books)
        {
            IList<BookViewModel> booksModel = new List<BookViewModel>();
            if (books != null)
            {
                for (int i = 0; i < books.Count; i++)
                {
                    if (books[i] != null)
                    {
                        var currentBook = new BookViewModel(books[i]);
                        if (i == 0)
                        {
                            UpdateBookDetailView(this, currentBook);
                        }
                        booksModel.Add(currentBook);
                    }
                }
            }
            _view.Books = booksModel;
        }

        private bool LoadBooks()
        {
            try
            {
                IEnumerable<ICanCreateGameBook> books;
                _session.ClearBooksList();
                using (var repo = _factory.NewStorage())
                {
                    books = repo.LoadBooksWithoutPages();
                }
                foreach (ICanCreateGameBook book in books)
                {
                    _session.AddBookInSession(book.AuthorName, book.IsbnRepresentation, book.TitleBook, book.Resume);
                }
                return true;
            }
            catch (Exception ex)
            {
                _view.HideChildren();
                _view.DisplayError(ex.Message);
                return false;
            }
        }

        private void UpdateBookDetailView(object? sender, BookViewModel book) => _view.BookDetail = book;
        private void FilterListBooks(object? sender, string filter) => AddBooksInBooksView(_session.GetFiltredBooks(filter).ToList());

        private void OnStartReading(object? sender, string isbn)
        {
            if (LoadPages(isbn))
            {
                _mainView.SwitchToPage("DisplayPageView");
            }
        }

        private bool LoadPages(string isbn)
        {
            IEnumerable<ICanCreateBookPage> pages;
            _session.UpdateCurrentBook(isbn);

            try
            {
                using (var repo = _factory.NewStorage())
                {
                    pages = repo.LoadPagesForABook(isbn);
                    AddPagesInBook(pages);
                }
                return true;
            }
            catch (Exception e)
            {
                _view.DisplayError(e.Message);
                return false;
            }

        }

        private void AddPagesInBook(IEnumerable<ICanCreateBookPage> pages)
        {
            var currentBook = _session.CurrentBook;

            if (currentBook != null)
            {
                foreach (var page in pages)
                {
                    currentBook.AddPageInBook(page);
                }
            }
        }
    }
}
