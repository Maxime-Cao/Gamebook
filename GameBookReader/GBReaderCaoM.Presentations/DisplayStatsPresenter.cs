using GBReaderCaoM.Domains;
using GBReaderCaoM.Presentations.Views;

namespace GBReaderCaoM.Presentations
{
    public class DisplayStatsPresenter
    {
        private readonly IDisplayStatsView _view;
        private readonly ISwitchToPages _mainView;
        private readonly ICanCreateReadSession _session;
        public DisplayStatsPresenter(IDisplayStatsView view, ISwitchToPages mainView, ICanCreateReadSession session)
        {
            if (view == null || mainView == null || session == null)
            {
                throw new ArgumentException("Votre vue, session et mainView doivent être correctement initialisées");
            }
            _view = view;
            _mainView = mainView;
            _session = session;

            _view.GoToHome += OnGoToHome;
            _view.LoadStats += OnLoadStats;
        }

        private void OnGoToHome(object? sender, EventArgs args) => _mainView.SwitchToPage("DisplayBooksView");

        private void OnLoadStats(object? sender, EventArgs args)
        {
            var userReadSessions = _session.UserReadSessions;

            if (userReadSessions != null && userReadSessions.Any())
            {
                var statsViewModel = BuildStatsViewModels(userReadSessions);
                AddStatsInView(statsViewModel);
            }
        }

        private IEnumerable<StatViewModel> BuildStatsViewModels(IEnumerable<ICanCreateUserReadSession> userReadSessions)
        {
            IList<StatViewModel> statsViewModels = new List<StatViewModel>();

            foreach (var userReadSession in userReadSessions)
            {
                var currentBook = _session.GetABook(userReadSession.CurrentBookIsbn);
                if (currentBook is not null)
                {
                    statsViewModels.Add(new StatViewModel(currentBook.TitleBook, userReadSession));
                }
            }

            return statsViewModels;
        }

        private void AddStatsInView(IEnumerable<StatViewModel> statsViewModel) => _view.Stats = statsViewModel;
    }
}
