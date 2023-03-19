namespace GBReaderCaoM.Domains
{
    public interface ICanCreateGameBook
    {
        ICanValidateIsbn Isbn { get; }

        string AuthorName { get; }

        string TitleBook { get; }

        string IsbnRepresentation { get; }

        string Resume { get; }

        void AddPageInBook(ICanCreateBookPage page);

        ICanCreateBookPage? GetPageWithNumber(int numberPage);
    }
}
