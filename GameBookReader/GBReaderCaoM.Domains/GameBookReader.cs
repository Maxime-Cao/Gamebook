using System.Text.RegularExpressions;

namespace GBReaderCaoM.Domains
{
    public class GameBookReader : ICanCreateGameBook
    {
        private readonly string _authorName;
        private readonly string _titleBook;
        private readonly ICanValidateIsbn _isbn;
        private readonly string _resume;
        private readonly List<ICanCreateBookPage> _pages = new(); // Ici j'ai besoin du type concret List (et pas IList) car je souhaite pouvoir avoir accès à la méthode .Sort() => voir méthode SortPage()

        public GameBookReader(string authorName, string isbn, string title, string resume)
        {
            ICanValidateIsbn newIsbn = new ISBN(isbn);

            ValidateCreationGameBook(title, resume, authorName);

            this._authorName = authorName;
            this._isbn = newIsbn;
            this._titleBook = title.Trim();
            this._resume = resume.Trim();
        }

        public GameBookReader(ICanCreateGameBook book)
        {
            if (book == null)
            {
                throw new ArgumentException("Le livre-jeu doit être dans un état cohérent et non null");
            }
            this._authorName = book.AuthorName;
            this._isbn = new ISBN(book.Isbn);
            this._titleBook = book.TitleBook;
            this._resume = book.Resume;
        }

        private void ValidateCreationGameBook(string title, string resume, string authorName)
        {
            ThrowExceptionOnValidateTitle(title);
            ThrowExceptionOnValidateResume(resume);
            ThrowExceptionOnValidateAuthorName(authorName);
        }

        private void ThrowExceptionOnValidateTitle(string title)
        {
            if (title == null || title.Length < 1 || title.Length > 150)
            {
                throw new ArgumentException("Votre titre doit contenir au minimum 1 caractère et au maximum 150 caractères");
            }
        }

        private void ThrowExceptionOnValidateResume(string resume)
        {
            if (string.IsNullOrWhiteSpace(resume) || resume.Length < 1 || resume.Length > 500)
            {
                throw new ArgumentException("Votre résumé doit contenir au moins 1 caractère et au maximum 500 caractères");
            }
        }

        private void ThrowExceptionOnValidateAuthorName(string authorName)
        {
            if (authorName == null || !Regex.IsMatch(authorName, @"^[a-zA-Z -\.]+$"))
            {
                throw new ArgumentException("Le nom de l'auteur doit contenir uniquement des lettres et ne pas être vide");
            }
        }

        public string AuthorName => _authorName;

        public string TitleBook => _titleBook;

        public string IsbnRepresentation => _isbn.IsbnRepresentation;

        public string Resume => _resume;

        public ICanValidateIsbn Isbn => _isbn;

        public void AddPageInBook(ICanCreateBookPage page)
        {
            if (page != null)
            {
                if (!_pages.Contains(page))
                {
                    AdaptPageNumber(page);
                    _pages.Add(page);
                    SortPage();
                }
            }
        }

        private void AdaptPageNumber(ICanCreateBookPage page)
        {
            var currentMaxNumPage = _pages.Count;
            if (page.NumberPage > currentMaxNumPage)
            {
                page.NumberPage = currentMaxNumPage + 1;
            }
        }

        public override bool Equals(object? obj) => obj is GameBookReader secondBook &&
                   _isbn.Equals(secondBook.Isbn);

        public override int GetHashCode() => HashCode.Combine(_isbn);

        private void SortPage() => _pages.Sort((firstPage, secondPage) => firstPage.NumberPage - secondPage.NumberPage);

        public ICanCreateBookPage? GetPageWithNumber(int numberPage)
        {
            foreach (var page in _pages)
            {
                if (page.NumberPage == numberPage)
                {
                    return page;
                }
            }
            return null;
        }
    }
}
