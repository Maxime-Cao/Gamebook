namespace GBReaderCaoM.Repositories.Exceptions
{
    public class ProviderNotFoundException : Exception
    {
        public ProviderNotFoundException(string message, Exception ex)
            : base(message, ex)
        { }
    }
}
