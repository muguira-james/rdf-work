package org.grandview;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;


import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

public class SimpleOps {

	public static void first() {
		Repository rep = new SailRepository(new MemoryStore());
		
		Namespace ex = Values.namespace("ex", "http://example.org/");
		IRI john = Values.iri(ex, "john");
		
		// RepositoryConnection conn = rep.getConnection();
		
		try (RepositoryConnection conn = rep.getConnection()) {
			conn.add(john, RDF.TYPE, FOAF.PERSON);
			conn.add(john, RDFS.LABEL, Values.literal("John"));
			
			RepositoryResult<Statement> statements = conn.getStatements(null, null, null);
			Model model = QueryResults.asModel(statements);
			
			model.setNamespace(RDF.NS);
			model.setNamespace(RDFS.NS);
			model.setNamespace(FOAF.NS);
			model.setNamespace(ex);
			
			Rio.write(model, System.out, RDFFormat.TURTLE);
		}
	}
	
	public static void second() {
		
		// We want to reuse this namespace when creating several building blocks.
		String ex = "http://example.org/";

		// Create IRIs for the resources we want to add.
		IRI picasso = iri(ex, "Picasso");
		IRI artist = iri(ex, "Artist");

		// Create a new, empty Model object.
		Model model = new TreeModel();

		// add our first statement: Picasso is an Artist
		model.add(picasso, RDF.TYPE, artist);

		// second statement: Picasso's first name is "Pablo".
		model.add(picasso, FOAF.FIRST_NAME, literal("Pablo"));

		// to see what's in our model, let's just print it to the screen
		for (Statement st : model) {
			System.out.println(st);
		}
	}

	public static void third() {
		ModelBuilder builder = new ModelBuilder();
		Model model = builder.setNamespace("ex", "http://example.org/")
		      .subject("ex:vanGoe")
		      .add(RDF.TYPE, "ex:Artist")
		      .add(FOAF.FIRST_NAME, "vinny")
		      .build();
		
		// to see what's in our model, let's just print it to the screen
		for (Statement st : model) {
			System.out.println(st);
		}
	}
	public static void main(String[] args) {
		System.out.println("ok");
		
		SimpleOps.first();
		System.out.println("\n\n");
		SimpleOps.second();
		System.out.println("\n\n");
		SimpleOps.third();
	}

}
