package org.ldbcouncil.snb.impls.workloads.postgres.operationhandlers;

import org.ldbcouncil.snb.driver.DbException;
import org.ldbcouncil.snb.driver.Operation;
import org.ldbcouncil.snb.driver.ResultReporter;
import org.ldbcouncil.snb.driver.workloads.interactive.LdbcNoResult;
import org.ldbcouncil.snb.impls.workloads.operationhandlers.UpdateOperationHandler;
import org.ldbcouncil.snb.impls.workloads.postgres.PostgresDbConnectionState;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class PostgresUpdateOperationHandler<TOperation extends Operation<LdbcNoResult>>
        implements UpdateOperationHandler<TOperation, PostgresDbConnectionState> {

    @Override
    public void executeOperation(TOperation operation, PostgresDbConnectionState state,
                                 ResultReporter resultReporter) throws DbException {
        try {
            Connection conn = state.getConnection();
            String queryString = getQueryString(state, operation);
                try (final Statement stmt = conn.createStatement()) {
                    state.logQuery(operation.getClass().getSimpleName(), queryString);
                    stmt.execute(queryString);
                } catch (Exception e) {
                    throw new DbException(e);
                }
                finally {
                    conn.close();
                }
                resultReporter.report(0, LdbcNoResult.INSTANCE, operation);
        }
        catch (SQLException e) {
            throw new DbException(e);
        }
    }

}
