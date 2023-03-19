namespace GBReaderCaoM.Presentations.Views
{
    public interface IDisplayBooksView : IPage
    {
        public event EventHandler<BookViewModel> BookSelected;
        public event EventHandler<string> FilterBooks;
        public event EventHandler<string> StartReading;
        public event EventHandler LoadBooksView;
        public event EventHandler GoToStats;

        public BookViewModel BookDetail { set; }
        public IEnumerable<BookViewModel> Books { set; }

        public void HideChildren();
    }
}
