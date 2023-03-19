namespace GBReaderCaoM.Repositories
{
    public interface IFactory
    {
        IGameBookRepository NewStorage();
    }
}
