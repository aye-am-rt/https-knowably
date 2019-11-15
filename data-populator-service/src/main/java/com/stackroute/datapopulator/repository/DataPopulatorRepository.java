package com.stackroute.datapopulator.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataPopulatorRepository extends Neo4jRepository {

    //----------------------creates nodes------------------------------------
    @Query("WITH apoc.convert.fromJsonMap({0}) AS row "+
            " UNWIND row.nodes AS node" +
            " WITH row,node,node.properties AS properties"+
            " CALL apoc.create.node(node.type,node.properties) YIELD node AS n" +
            " SET n.id=node.uuid"+
            " SET n.label=node.type")
    public void createNodes(String line);

    //---------------------creates relationships----------------------------
    @Query("WITH apoc.convert.fromJsonMap({0}) AS row "+
            " UNWIND row.relationship AS rel" +
            " MATCH (a) WHERE a.id = rel.sourcenode"+
            " MATCH (b) WHERE b.id = rel.destnode"+
            " CALL apoc.create.relationship(a,rel.relation,rel.properties, b) YIELD rel AS r"+
            " SET r.id=rel.uuid"+
            " SET r.label=rel.relation")
    public void createRelations(String line);

    //-----------------------merges dupliacte data--------------------------------
    @Query(" MATCH (n)"+
            " WITH n.name AS name, COLLECT(n) AS nodelist, COUNT(*) AS count"+
            " WHERE count > 1"+
            " CALL apoc.refactor.mergeNodes(nodelist) YIELD node"+
            " SET node.id=node.id "
    )
    public void mergeNodes();
    @Query(
            " MATCH (A)-[r]->(B)"+
                    " WITH  A,B,collect(distinct(r.weight)) as values, count(r) as relsCount"+
                    " MATCH (A)-[r]->(B)"+
                    " WHERE relsCount > 1"+
                    " WITH A,B,collect(r) as rels"+
                    " CALL apoc.refactor.mergeRelationships(rels,{properties:\"combine\"})"+
                    " YIELD rel SET rel.id=rel.id"
    )
    public void mergeRelations();

    //-----------------------returns all node labels--------------------------------
    @Query(" MATCH (n)"+
            " RETURN DISTINCT labels(n)")
    public List<String> getNodeLabels();

    //------------------------returns all relation labels----------------------------
    @Query(" MATCH ()-[r]-()"+
            " RETURN DISTINCT type(r)")
    public List<String> getRelations();

    //------------------------returns matching node names-----------------------------
    @Query("MATCH (n) WHERE {0} IN labels(n)"+"RETURN [n.name]")
    public List<String> getNodeNames(String param);
}
