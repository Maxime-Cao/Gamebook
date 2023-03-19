namespace GBReaderCaoM.Domains
{
    public interface ICanCreateBookPage
    {
        void AddNewChoice(string content, ICanCreateBookPage page);
        int NumberPage { get; set; }

        string Content { get; }

        IEnumerable<string> Choices { get; }

        ICanCreateBookPage? GetTargetPage(string textChoice);

        bool IsEndPage();
    }
}
