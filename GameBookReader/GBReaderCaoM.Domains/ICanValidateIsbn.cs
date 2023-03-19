namespace GBReaderCaoM.Domains
{
    public interface ICanValidateIsbn
    {
        public string IsbnRepresentation { get; }

        bool ValidateLanguageNumber(string language);

        bool ValidateIdBook(int idBook);

        bool ValidateMatricule(string matriculeToCheck);

        string GetControlCode(string numberToCheck);
    }
}
