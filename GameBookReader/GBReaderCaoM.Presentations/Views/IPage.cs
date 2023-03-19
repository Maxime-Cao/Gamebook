namespace GBReaderCaoM.Presentations.Views
{
    public interface IPage
    {
        void DisplayInfo(string info);
        void DisplayError(string error);

        void RefreshView();
    }
}
