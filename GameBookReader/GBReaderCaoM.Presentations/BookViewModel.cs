using GBReaderCaoM.Domains;

namespace GBReaderCaoM.Presentations
{
    public record BookViewModel
    {
        private readonly ICanCreateGameBook _bookReader;
        public BookViewModel(ICanCreateGameBook book)
        {
            _bookReader = book ?? throw new ArgumentException("Votre instance de ICanCreateGameBook ne doit pas être nulle");
        }

        public string AuthorName => _bookReader.AuthorName;

        public string TitleBook => _bookReader.TitleBook;

        public string Isbn => _bookReader.IsbnRepresentation;

        public string Resume => _bookReader.Resume;
    }
}
