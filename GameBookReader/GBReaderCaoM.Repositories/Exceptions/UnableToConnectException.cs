namespace GBReaderCaoM.Repositories.Exceptions
{
    public class UnableToConnectException : Exception
    {
        public UnableToConnectException(string message, Exception ex)
            : base(message, ex)
        { }

        public UnableToConnectException(string message)
            : base(message)
        { }
    }
}
