namespace GBReaderCaoM.Repositories.Exceptions
{
    public class LoadPageException : Exception
    {
        public LoadPageException(string message, Exception ex)
: base(message, ex)
        { }
    }
}
