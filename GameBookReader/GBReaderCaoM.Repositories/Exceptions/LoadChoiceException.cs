namespace GBReaderCaoM.Repositories.Exceptions
{
    public class LoadChoiceException : Exception
    {
        public LoadChoiceException(string message, Exception ex)
: base(message, ex)
        { }
    }
}
