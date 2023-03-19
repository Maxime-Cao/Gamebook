using System.Text.RegularExpressions;

namespace GBReaderCaoM.Domains
{
    internal class ISBN : ICanValidateIsbn
    {
        private readonly string _isbnString;

        public string IsbnRepresentation => _isbnString;

        public ISBN(string isbn)
        {
            ThrowExceptionOnValidateStringIsbn(isbn);
            int idBook = int.Parse(isbn.Substring(9, 2));
            string language = isbn[..1];
            string idAuthor = isbn.Substring(2, 6);
            ValidateIsbn(language, idAuthor, idBook);
            string isbnToControl = string.Format("{0}{1}{2:00}", language, idAuthor, idBook);
            ThrowExceptionOnValidateControlCode(isbnToControl, isbn[^1..]);
            this._isbnString = string.Format("{0}-{1}-{2:00}-{3}", language, idAuthor, idBook, GetControlCode(isbnToControl));
        }

        public ISBN(ICanValidateIsbn isbnToCopy)
        {
            if (isbnToCopy == null)
            {
                throw new ArgumentException("Le numéro ISBN doit être dans un état cohérent et non null");
            }
            this._isbnString = isbnToCopy.IsbnRepresentation;
        }

        private void ThrowExceptionOnValidateStringIsbn(string isbn)
        {
            ThrowExceptionOnValidateLengthFormattedISBN(isbn);
            ThrowExceptionOnValidateTypeIdBook(isbn);
        }

        private void ValidateIsbn(string language, string idAuthor, int idBook)
        {
            ThrowExceptionOnValidateBlankFields(language, idAuthor);
            ThrowExceptionOnValidateLanguage(language);
            ThrowExceptionOnValidateIdAuthor(idAuthor);
            ThrowExceptionOnValidateIdBook(idBook);
        }

        private void ThrowExceptionOnValidateBlankFields(string language, string idAuthor)
        {
            if (string.IsNullOrWhiteSpace(language) || string.IsNullOrWhiteSpace(idAuthor))
            {
                throw new ArgumentException("Tous les champs doivent être remplis");
            }
        }

        private void ThrowExceptionOnValidateLanguage(string language)
        {
            if (!ValidateLanguageNumber(language))
            {
                throw new ArgumentException("Le numéro du groupe linguistique doit être compris entre 0 et 4 ou égal à 7");
            }
        }

        private void ThrowExceptionOnValidateIdAuthor(string idAuthor)
        {
            if (!ValidateMatricule(idAuthor))
            {
                throw new ArgumentException("L'identifiant de l'auteur n'est pas correct");
            }
        }

        private void ThrowExceptionOnValidateIdBook(int idBook)
        {
            if (!ValidateIdBook(idBook))
            {
                throw new ArgumentException("L'identifiant du livre doit être compris entre 1 et 99");
            }
        }

        public bool ValidateLanguageNumber(string language)
        {
            try
            {
                int number = int.Parse(language);
                return (number >= 0 && number < 5) || number == 7;
            }
            catch (FormatException)
            {
                return false;
            }
        }

        public bool ValidateMatricule(string matriculeToCheck) => Regex.IsMatch(matriculeToCheck, "^[0-9]{6}$");

        public bool ValidateIdBook(int numberToCheck)
        {
            try
            {
                return numberToCheck >= 1 && numberToCheck < 100;
            }
            catch (FormatException)
            {
                return false;
            }
        }

        private void ThrowExceptionOnValidateLengthFormattedISBN(string isbn)
        {
            if (isbn == null || isbn.Length != 13)
            {
                throw new ArgumentException("Le numéro ISBN ne contient pas le bon nombre de caractères");
            }
        }
        private void ThrowExceptionOnValidateTypeIdBook(string isbn)
        {
            try
            {
                int.Parse(isbn.Substring(9, 2));
            }
            catch (FormatException)
            {
                throw new ArgumentException("L'identifiant du livre doit être un nombre");
            }
        }

        private void ThrowExceptionOnValidateControlCode(string isbnToControl, string givenControlCode)
        {
            string calculatedControlCode = GetControlCode(isbnToControl);
            if (!calculatedControlCode.Equals(givenControlCode))
            {
                throw new ArgumentException("Le code de contrôle n'est pas correct");
            }
        }

        public string GetControlCode(string isbnToCheck)
        {
            if (isbnToCheck == null || isbnToCheck.Length != 9)
            {
                throw new ArgumentException("Le calcul du code de contrôle se fait uniquement sur un ISBN à 9 chiffres");
            }
            try
            {
                int somme = CalcuteSumControlCode(isbnToCheck);
                somme = 11 - (somme % 11);
                return somme == 10 ? "X" : somme == 11 ? "0" : somme.ToString();
            }
            catch (FormatException)
            {
                throw new ArgumentException("L'ISBN ne doit contenir que des chiffres");
            }
        }

        private int CalcuteSumControlCode(string isbnToCheck)
        {
            int somme = 0;
            int poids = 10;
            for (int i = 0; i < 9; i++, poids--)
            {
                somme += poids * int.Parse(isbnToCheck.Substring(i, 1));
            }
            return somme;
        }

        public override bool Equals(object? obj) => obj is ISBN secondIsbn && _isbnString.Equals(secondIsbn._isbnString);

        public override int GetHashCode() => HashCode.Combine(_isbnString);
    }
}
