namespace GBReaderCaoM.Repositories.Exceptions
{
    public class SaveUserReadSessionException : Exception
    {
        public SaveUserReadSessionException(string message, Exception ex)
: base(message, ex)
        { }
    }
}
