using GBReaderCaoM.Repositories;
using GBReaderCaoM.Repositories.Exceptions;
using System.Data;
using System.Data.Common;

namespace GBReaderCaoM.Infrastructures.DB
{
    public class SqlProjectStorageFactory : IFactory
    {
        private readonly DbProviderFactory _factory;
        private readonly string _connectionString;
        private readonly UpdateHandler _updateHandler;

        public SqlProjectStorageFactory(IConnectionData connectionData, UpdateHandler updateHandler)
        {
            try
            {
                DbProviderFactories.RegisterFactory(connectionData.ProviderName, connectionData.DbProviderFactory);
                _factory = DbProviderFactories.GetFactory(connectionData.ProviderName);
                _connectionString = $"server={connectionData.DataSource};uid={connectionData.UserId};pwd={connectionData.Password};database={connectionData.DBName}";
                _updateHandler = updateHandler;
            }
            catch (ArgumentException ex)
            {
                throw new ProviderNotFoundException($"Unable to load provider {connectionData.ProviderName}", ex);
            }
        }

        public IGameBookRepository NewStorage()
        {
            try
            {
                IDbConnection? con = _factory.CreateConnection();
                if (con != null)
                {
                    con.ConnectionString = _connectionString;
                    con.Open();
                    return new SqlProjectStorage(con, _updateHandler);
                }
                else
                {
                    throw new UnableToConnectException("Impossible d'établir une connexion à l'espace de stockage");
                }
            }
            catch (ArgumentException ex)
            {
                throw new InvalidConnectionStringException("Unable to use this connection string", ex);
            }
            catch (DbException ex)
            {
                throw new UnableToConnectException("Impossible de récupérer les couvertures des livres", ex);
            }
        }
    }
}