namespace GBReaderCaoM.Repositories.Exceptions
{
    public class LoadUserReadSessionException : Exception
    {
        public LoadUserReadSessionException(string message, Exception ex)
: base(message, ex)
        { }
    }
}
