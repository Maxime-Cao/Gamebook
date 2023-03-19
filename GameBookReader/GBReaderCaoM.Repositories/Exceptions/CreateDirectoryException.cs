namespace GBReaderCaoM.Repositories.Exceptions
{
    public class CreateDirectoryException : Exception
    {
        public CreateDirectoryException(string message, Exception ex)
: base(message, ex)
        { }
    }
}
