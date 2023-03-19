using Avalonia;
using Avalonia.Controls.ApplicationLifetimes;
using Avalonia.Markup.Xaml;
using GBReaderCaoM.Avalonia.Pages;
using GBReaderCaoM.Domains;
using GBReaderCaoM.Infrastructures.DB;
using GBReaderCaoM.Infrastructures.JsonFile;
using GBReaderCaoM.Presentations;

namespace GBReaderCaoM.Avalonia
{
    public partial class App : Application
    {
        private MainWindow? _mainWindow;
        public override void Initialize() => AvaloniaXamlLoader.Load(this);

        public override void OnFrameworkInitializationCompleted()
        {
            if (ApplicationLifetime is IClassicDesktopStyleApplicationLifetime desktop)
            {
                _mainWindow = new MainWindow();
                desktop.MainWindow = _mainWindow;
                CreateViewsAndPresenters();
            }

            base.OnFrameworkInitializationCompleted();
        }

        private void CreateViewsAndPresenters()
        {
            if (_mainWindow != null)
            {
                var readSession = new ReadSession();
                var factory = new SqlProjectStorageFactory(new MySQLConnectionData(), new UpdateHandler());
                var repository = new JsonSaveSessionRepository();

                var displayBooksView = new DisplayBooksView { SearchView = new SearchView(), BookDetailsView = new BookDetailsView() };
                _ = new DisplayBooksPresenter(displayBooksView, readSession, factory, _mainWindow, repository);

                var displayPageView = new DisplayPageView();
                _ = new DisplayPagePresenter(displayPageView, _mainWindow, readSession, repository);

                var displayStatsView = new DisplayStatsView();
                _ = new DisplayStatsPresenter(displayStatsView, _mainWindow, readSession);

                _mainWindow.AddPage(displayBooksView.GetType().Name, displayBooksView);
                _mainWindow.AddPage(displayPageView.GetType().Name, displayPageView);
                _mainWindow.AddPage(displayStatsView.GetType().Name, displayStatsView);

                _mainWindow.SwitchToPage(displayBooksView.GetType().Name);
            }
        }
    }
}
