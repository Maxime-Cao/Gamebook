using GBReaderCaoM.Domains;
using GBReaderCaoM.Presentations.Views;
using GBReaderCaoM.Repositories;

namespace GBReaderCaoM.Presentations
{
    public class DisplayPagePresenter
    {
        private readonly IDisplayPage _view;
        private readonly ISwitchToPages _mainView;
        private readonly ICanCreateReadSession _session;
        private readonly IUserReadSessionRepository _sessionsRepository;
        private ICanCreateBookPage? _currentPage;

        public DisplayPagePresenter(IDisplayPage view, ISwitchToPages mainView, ICanCreateReadSession session, IUserReadSessionRepository sessionsRepository)
        {
            if (view == null || mainView == null || session == null || sessionsRepository == null)
            {
                throw new ArgumentException("Votre vue, session, repository et mainView doivent être correctement initialisés");
            }
            _view = view;
            _mainView = mainView;
            _session = session;
            _sessionsRepository = sessionsRepository;

            _view.LoadPageView += OnLoadView;
            _view.GoToHome += OnGoToHome;
            _view.ChoiceSelected += OnChoiceSelected;
            _view.RestartReading += OnRestartReading;
        }

        private void OnRestartReading(object? sender, EventArgs e)
        {
            var currentBook = _session.CurrentBook;
            if (currentBook != null)
            {
                var page = currentBook.GetPageWithNumber(1);
                if (page != null)
                {
                    LoadPageInView(page);
                }
            }
        }

        private void OnLoadView(object? sender, EventArgs args) => GetCurrentPage();

        private void GetCurrentPage()
        {
            var currentBook = _session.CurrentBook;

            if (currentBook != null)
            {
                int numberPage = _session.GetCurrentPageUserSession();
                var page = currentBook.GetPageWithNumber(numberPage);
                page = page == null || page.IsEndPage() ? currentBook.GetPageWithNumber(1) : page;

                if (page is not null)
                {
                    OnPageFoundInBook(page);
                    LoadPageInView(page);
                }
            }
        }

        private void OnPageFoundInBook(ICanCreateBookPage page)
        {
            if (page?.NumberPage == 1)
            {
                _session.DeleteUserReadSession();
                SaveUserSessionsInStorage();
            }
        }

        private void LoadPageInView(ICanCreateBookPage currentPage)
        {
            if (currentPage != null)
            {
                _currentPage = currentPage;
                var pageViewModel = new PageViewModel(currentPage);
                _view.Page = pageViewModel;
            }
        }

        private void OnGoToHome(object? sender, EventArgs args) => _mainView.SwitchToPage("DisplayBooksView");

        private void OnChoiceSelected(object? sender, string textChoice)
        {
            if (_currentPage != null)
            {
                var targetPage = _currentPage.GetTargetPage(textChoice);
                if (targetPage != null)
                {
                    SaveUserSession(targetPage);
                    LoadPageInView(targetPage);
                }
            }
        }

        private void SaveUserSession(ICanCreateBookPage targetPage)
        {
            if (targetPage.IsEndPage() || targetPage.NumberPage == 1)
            {
                _session.DeleteUserReadSession();
            }
            else
            {
                _session.AddUserReadSession(targetPage.NumberPage);
            }

            SaveUserSessionsInStorage();
        }

        private void SaveUserSessionsInStorage()
        {
            try
            {
                _sessionsRepository.SaveUserReadSessions(_session.UserReadSessions);
            }
            catch (Exception e)
            {
                _view.DisplayError(e.Message);
            }
        }
    }

}
