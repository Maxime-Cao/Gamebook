namespace GBReaderCaoM.Domains
{
    public class Page : ICanCreateBookPage
    {
        private readonly int _numberPage;
        private readonly string _content;
        private readonly IDictionary<string, ICanCreateBookPage> _choices = new Dictionary<string, ICanCreateBookPage>();

        public Page(int numberPage, string content)
        {
            _numberPage = Math.Max(numberPage, 1);
            if (string.IsNullOrEmpty(content))
            {
                throw new ArgumentException("Le texte de la page ne doit pas être null ou vide");
            }
            else
            {
                _content = content.Trim();
            }
        }

        public void AddNewChoice(string content, ICanCreateBookPage page)
        {
            CheckNullOrEmptyChoice(content, page);
            CheckIfChoiceIsCorrect(content);
            _choices[content] = page;
        }

        private void CheckNullOrEmptyChoice(string content, ICanCreateBookPage page)
        {
            if (string.IsNullOrEmpty(content) || page == null)
            {
                throw new ArgumentException("Le texte du choix ne peut pas être vide");
            }
        }

        private void CheckIfChoiceIsCorrect(string content)
        {
            foreach (string choiceContent in _choices.Keys)
            {
                if (choiceContent.Equals(content))
                {
                    throw new ArgumentException("Un autre choix de la page existe déjà avec le même texte");
                }
            }
        }

        public int NumberPage
        {
            get => _numberPage;
            set => Math.Max(value, 1);
        }

        public string Content => _content;

        public IEnumerable<string> Choices => _choices.Keys.ToList();

        public override bool Equals(object? obj) => obj is Page secondPage &&
           _numberPage == secondPage.NumberPage;

        public override int GetHashCode() => HashCode.Combine(_numberPage);

        public ICanCreateBookPage? GetTargetPage(string textChoice)
        {
            if (_choices.ContainsKey(textChoice))
            {
                return _choices[textChoice];
            }
            return null;
        }

        public bool IsEndPage() => _choices.Count == 0;
    }
}
