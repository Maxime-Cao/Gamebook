using GBReaderCaoM.Domains;

namespace GBReaderCaoM.Presentations
{
    public record PageViewModel
    {
        private readonly ICanCreateBookPage _page;

        public PageViewModel(ICanCreateBookPage page)
        {
            _page = page ?? throw new ArgumentException("Votre instance de ICanCreateBookPage ne doit pas être nulle");
        }

        public int NumberPage => _page.NumberPage;

        public string TextPage => _page.Content;

        public IEnumerable<string> Choices => _page.Choices;

        public bool IsEndPage => _page.IsEndPage();
    }
}
