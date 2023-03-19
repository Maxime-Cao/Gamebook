namespace GBReaderCaoM.Presentations.Views
{
    public interface IDisplayStatsView : IPage
    {
        public event EventHandler GoToHome;
        public event EventHandler LoadStats;

        public IEnumerable<StatViewModel> Stats { set; }
    }
}
