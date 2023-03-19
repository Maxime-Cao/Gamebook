using GBReaderCaoM.Domains;

namespace GBReaderCaoM.Repositories
{
    public interface IGameBookRepository : IDisposable
    {
        IEnumerable<ICanCreateGameBook> LoadBooksWithoutPages();
        IEnumerable<ICanCreateBookPage> LoadPagesForABook(string isbn);
    }
}
