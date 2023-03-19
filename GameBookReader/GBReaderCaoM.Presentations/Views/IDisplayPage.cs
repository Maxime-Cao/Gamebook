namespace GBReaderCaoM.Presentations.Views
{
    public interface IDisplayPage : IPage
    {
        event EventHandler GoToHome;
        event EventHandler<string> ChoiceSelected;
        event EventHandler LoadPageView;
        event EventHandler RestartReading;
        PageViewModel Page { set; }
    }
}
