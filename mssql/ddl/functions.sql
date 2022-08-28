/* http://hansolav.net/sql/graphs.html#bfs
 * Runs breadth-first search from a specific node.
 * @StartNode: If of node to start the search at.
 * @EndNode: Stop the search when node with this id is found. Specify NULL
 * 			 to traverse the whole graph.
*/

CREATE PROCEDURE dbo.knows_Breadth_First (@StartNode bigint, @EndNode bigint = NULL)
AS
BEGIN
    SET XACT_ABORT ON    
    BEGIN TRAN
    
    SET NOCOUNT ON;

	CREATE TABLE #Discovered
	(
		Id bigint NOT NULL,
		Predecessor bigint NULL,
		OrderDiscovered bigint
	)

    INSERT INTO #Discovered (Id, Predecessor, OrderDiscovered)
    VALUES (@StartNode, NULL, 0)

	WHILE @@ROWCOUNT > 0
    BEGIN
		IF @EndNode IS NOT NULL
			IF EXISTS (SELECT TOP 1 1 FROM #Discovered WHERE Id = @EndNode)
				BREAK    
    
		-- We need to get all shortest path and then choose the one with the lowest weight
		INSERT INTO #Discovered (Id, Predecessor, OrderDiscovered)
		SELECT e.person2id, MIN(e.person1id), MIN(d.OrderDiscovered) + 1
		FROM #Discovered d JOIN dbo.Person_knows_Person e ON d.Id = e.person1id
		WHERE e.person2id NOT IN (SELECT Id From #Discovered)
		GROUP BY e.person1id, e.person2id
    END;

	WITH BacktraceCTE(Id, OrderDiscovered, Path)
	AS
	(
		SELECT n.personId, d.OrderDiscovered, CAST(n.personId AS varchar(MAX))
		FROM #Discovered d JOIN dbo.Person n ON d.Id = n.personId
		WHERE d.Id = @StartNode
		
		UNION ALL

		SELECT n.personId, d.OrderDiscovered,
			CAST(cte.Path + ';' + CAST(n.personId as varchar(MAX)) as varchar(MAX))
		FROM #Discovered d JOIN BacktraceCTE cte ON d.Predecessor = cte.Id
		JOIN dbo.Person n ON d.Id = n.personId
	)
	
	SELECT Id, OrderDiscovered, Path FROM BacktraceCTE
	WHERE Id = @EndNode OR @EndNode IS NULL
	ORDER BY OrderDiscovered
    
    DROP TABLE #Discovered
    COMMIT TRAN
    RETURN 0
END;