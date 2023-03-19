namespace GBReaderCaoM.Repositories.Exceptions
{
    public class LoadBookException : Exception
    {
        public LoadBookException(string message, Exception ex)
: base(message, ex)
        { }
    }
}
