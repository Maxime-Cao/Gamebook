namespace GBReaderCaoM.Repositories.Exceptions
{
    public class InvalidConnectionStringException : Exception
    {
        public InvalidConnectionStringException(string message, Exception ex)
        : base(message, ex)
        { }
    }
}
