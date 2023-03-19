using GBReaderCaoM.Domains;

namespace GBReaderCaoM.Presentations
{
    public record StatViewModel
    {
        private readonly ICanCreateUserReadSession _userReadSession;
        private readonly string _titleBook;

        public StatViewModel(string titleBook, ICanCreateUserReadSession userReadSession)
        {
            _userReadSession = userReadSession ?? throw new ArgumentException("Votre instance de ICanCreateUserReadSession ne doit pas être nulle");
            _titleBook = titleBook ?? throw new ArgumentException("Le titre du livre ne doit pas être null");
        }

        public string TitleBook => _titleBook;

        public int NumberPage => _userReadSession.CurrentPageNumber;

        public string FirstRead => _userReadSession.FirstRead.ToString();

        public string LastRead => _userReadSession.LastRead.ToString();

        public string IsbnBook => _userReadSession.CurrentBookIsbn;
    }
}
