package org.helmo.gbeditor.infrastructures.jdbc;

import org.helmo.gbeditor.repositories.exceptions.UnableToRollbackException;

import java.sql.Connection;
import java.sql.SQLException;
@SuppressWarnings("PMD")
/**
 * Cette classe provient du cours de structure de données de Monsieur Ludewig
 * @author François Ludewig
  */
public class Transaction {
    private final Connection con;
    private ExceptionHandle rollbackAction;
    private ActionThrowingException commitAction;

    /**
     *
     * @param con
     * @return
     */
    public static Transaction from(Connection con) {
        try {
            con.setAutoCommit(false);
        } catch (SQLException ex) {
            throw new TransactionNotSupportedException(ex);
        }
        return new Transaction(con);
    }

    /**
     *
     * @param con
     */
    public Transaction(Connection con) {
        this.con = con;
    }

    /**
     *
     * @param sequence
     * @return
     */
    public Transaction commit(ActionThrowingException sequence) {
        this.commitAction = sequence;
        return this;
    }

    /**
     *
     * @param sequence
     * @return
     */
    public Transaction onRollback(ExceptionHandle sequence) {
        this.rollbackAction = sequence;
        return this;
    }

    /**
     *
     */
    public void execute() {
        try {
            commitAction.execute(con);
            con.commit();
        } catch (Exception ex) {
            try {
                con.rollback();
                rollbackAction.handle(ex);
            } catch (SQLException e) {
                throw new UnableToRollbackException(e);
            }
        } finally {
            try {
                con.setAutoCommit(true); //Active la gestion automatique des transactions
            } catch(SQLException ex) {
                throw new TransactionNotSupportedException(ex);
            }
        }
    }
}

/**
 *
 */
class TransactionNotSupportedException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    /**
     *
     * @param ex
     */
	public TransactionNotSupportedException(SQLException ex) {
        super("Transaction are not supported by this DBMS or this driver", ex);
    }
}

/**
 *
 */
@FunctionalInterface
/**
 *
 */
interface ActionThrowingException {
    /**
     *
     * @param con
     * @throws Exception
     */
    void execute(Connection con) throws Exception;
}

/**
 *
 */
@FunctionalInterface
/**
 *
 */
interface ExceptionHandle {
    /**
     * 
     * @param ex
     */
    void handle(Exception ex);
}
