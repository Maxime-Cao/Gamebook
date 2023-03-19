using GBReaderCaoM.Domains;
using GBReaderCaoM.Repositories;
using GBReaderCaoM.Repositories.Exceptions;
using System.Data;

namespace GBReaderCaoM.Infrastructures.DB
{
    public class SqlProjectStorage : IGameBookRepository
    {
        private readonly IDbConnection _con;
        private readonly UpdateHandler _updateHandler;

        public SqlProjectStorage(IDbConnection con, UpdateHandler updateHandler)
        {
            _con = con;
            _updateHandler = updateHandler;
        }
        public IEnumerable<ICanCreateGameBook> LoadBooksWithoutPages()
        {
            IList<ICanCreateGameBook> booksFound = new List<ICanCreateGameBook>();
            _updateHandler.ClearBooksMap();
            try
            {
                string selectQuery = @"SELECT gbb.idBook,gbb.isbn,gbb.title,gbb.resume,gba.firstname,gba.name FROM gb_book gbb JOIN gb_author gba ON gbb.idAuthor = gba.idAuthor WHERE gbb.publishState = @publishState";
                using var selectedCmd = _con.CreateCommand();
                selectedCmd.CommandText = selectQuery;
                selectedCmd.Parameters.Add(CreateParameter(selectedCmd.CreateParameter(), "@publishState", 1, DbType.Int64));
                using IDataReader reader = selectedCmd.ExecuteReader();
                while (reader.Read())
                {
                    try
                    {
                        booksFound.Add(new GameBookReader($"{(string)reader["firstname"]} {(string)reader["name"]}", (string)reader["isbn"], (string)reader["title"], (string)reader["resume"]));
                        _updateHandler.AddBookInList((int)reader["idBook"], (string)reader["isbn"]);
                    }
                    catch (ArgumentException)
                    {
                        continue;
                    }
                }
            }
            catch (Exception e)
            {
                // La documentation ne référence pas d'exception spécifique déclenchée lors de ce traitement, j'ai donc choisi de capturer "Exception", avec l'accord de Monsieur Hendrikx
                throw new LoadBookException("Impossible de charger le(s) livre(s) publié(s)", e);
            }
            return booksFound;
        }

        private IDbDataParameter CreateParameter(IDbDataParameter createParameter, string name, object value, DbType dbType = DbType.String)
        {
            createParameter.ParameterName = name;
            createParameter.Value = value;
            createParameter.DbType = dbType;
            return createParameter;
        }

        public IEnumerable<ICanCreateBookPage> LoadPagesForABook(string isbn)
        {
            int idBook = _updateHandler.GetIdBookWithIsbn(isbn);
            IList<ICanCreateBookPage> pages = new List<ICanCreateBookPage>();

            if (idBook > 0)
            {
                LoadPages(idBook);
                LoadChoicesForPages();
                pages = _updateHandler.Pages;
            }
            return pages;
        }

        private void LoadPages(int idBook)
        {
            _updateHandler.ClearPagesMap();
            IList<ICanCreateBookPage> pagesFound = new List<ICanCreateBookPage>();
            try
            {
                string selectQuery = @"SELECT idPage,numberPage,textPage FROM gb_page WHERE idBook = @idBook";
                using var selectedCmd = _con.CreateCommand();
                selectedCmd.CommandText = selectQuery;
                selectedCmd.Parameters.Add(CreateParameter(selectedCmd.CreateParameter(), "@idBook", idBook, DbType.Int64));
                using IDataReader reader = selectedCmd.ExecuteReader();
                while (reader.Read())
                {
                    try
                    {
                        ICanCreateBookPage page = new Page((int)reader["numberPage"], (string)reader["textPage"]);
                        pagesFound.Add(page);
                        _updateHandler.AddPageInList((int)reader["idPage"], page);
                    }
                    catch (ArgumentException)
                    {
                        continue;
                    }
                }
            }
            catch (Exception e)
            {
                throw new LoadPageException("Impossible de charger les pages du livre", e);
            }
        }

        private void LoadChoicesForPages()
        {
            foreach (var page in _updateHandler.PagesMap)
            {
                LoadChoicesForOnePage(page.Key, page.Value);
            }
        }

        private void LoadChoicesForOnePage(int idPage, ICanCreateBookPage page)
        {
            try
            {
                string selectQuery = @"SELECT textChoice,idPageDest FROM gb_choice WHERE idPage = @idPage";
                using var selectedCmd = _con.CreateCommand();
                selectedCmd.CommandText = selectQuery;
                selectedCmd.Parameters.Add(CreateParameter(selectedCmd.CreateParameter(), "@idPage", idPage, DbType.Int64));
                using IDataReader reader = selectedCmd.ExecuteReader();
                while (reader.Read())
                {
                    try
                    {
                        ICanCreateBookPage? pageDest = _updateHandler.GetPageWithId((int)reader["idPageDest"]);
                        if (pageDest != null)
                        {
                            page.AddNewChoice((string)reader["textChoice"], pageDest);
                        }
                    }
                    catch (ArgumentException)
                    {
                        continue;
                    }
                }
            }
            catch (Exception e)
            {
                throw new LoadChoiceException($"Impossible de charger les choix de la page {page.NumberPage}", e);
            }
        }

        public void Dispose() => _con.Dispose();
    }
}
