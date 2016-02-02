package miningTweet;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;

import org.jgrapht.graph.*;

public class TweetGraph {
	//private int DimensionX;
	//private int DimensionY;
	
	private Map<VertexPair,Integer> tagMap;
	
	//private HashSet<HashSet<String>> tf;
	protected ListenableUndirectedWeightedGraph<String, DefaultWeightedEdge>  graph = 
            new ListenableUndirectedWeightedGraph<String, DefaultWeightedEdge>
            (DefaultWeightedEdge.class); 
	
	private Set<String> vertexSet = new HashSet<String>();
	
	
	
	/*public TweetGraph(Set<String> vertexSet, Map<VertexPair,Integer> weightedEdge ){
		//this.vertexSet = new HashSet<String>(vertexSet);
		//this.tagMap = weightedEdge;
		
		//this.buildGraph();
	}*/
	
	public TweetGraph(ListenableUndirectedWeightedGraph<String, DefaultWeightedEdge> g){
		this.graph = g;
	}
	public TweetGraph(){
		
	}
	
	
	public boolean removeVertexAndEdges(String vertex){
		
		if (this.graph.containsVertex(vertex)){
			List<DefaultWeightedEdge> edgeList = new ArrayList<DefaultWeightedEdge>();
			for (DefaultWeightedEdge e: this.graph.edgesOf(vertex)){
				edgeList.add(e);
			}
			for(DefaultWeightedEdge e:edgeList)
				this.graph.removeEdge(e);
			this.graph.removeVertex(vertex);
			return true;
		}
		
		else return false;
	}

	
	public Map<VertexPair,Integer> getTagMap(HashSet<HashSet<String>> tf){
		Map<VertexPair,Integer> tagPairMap = new HashMap<VertexPair, Integer>();
		for (HashSet<String> hashtags : tf){
			ArrayList<String> cachedTags =  new ArrayList<String>(hashtags);
			Iterator<String> hashTagsIt = hashtags.iterator();
			while (hashTagsIt.hasNext()){
				//System.out.println(tag1);
				String tag1 = (String) hashTagsIt.next();
				//vertexSet.add(tag1);
				this.graph.addVertex(tag1);
				cachedTags.remove(tag1);
				for (String tag2: cachedTags){
					
					VertexPair vp = new VertexPair(tag1,tag2);	//use lower case for each vertex
					this.graph.addVertex(tag2);
					/*vertexSet.add(tag2);
					tagPairMap.put(vp,((tagPairMap.get(vp)==null) ? 0 : tagPairMap.get(vp)) + 1);
					*/
					if (this.graph.containsEdge(vp.getVertex1(), vp.getVertex2())){
						DefaultWeightedEdge e = this.graph.getEdge(vp.getVertex1(), vp.getVertex2());
						graph.setEdgeWeight(e,this.graph.getEdgeWeight(e)+1);
					}else{
						DefaultWeightedEdge e = this.graph.addEdge(vp.getVertex1(), vp.getVertex2());
						graph.setEdgeWeight(e, 1);
					}
					/*if (tagPairMap.containsKey(vp)){
						//System.out.println("contains");
						tagPairMap.put(vp, tagPairMap.get(vp)+1);
					}else
						tagPairMap.put(vp, 1);
					*/
				}
			}
		}
		this.tagMap = tagPairMap;
		return tagPairMap;
	}
	/*public TweetGraph(TweetFilter tf) throws IOException{
		this.tf = tf.filter();
	}*/
	
	//buildGraph is integreated in getTagMap method
	/*public void buildGraph(){
		for (String vertex:vertexSet){
			graph.addVertex(vertex);
		}
		//System.out.println(graph.toString());
		for (VertexPair edge:tagMap.keySet()){
			
			DefaultWeightedEdge e = graph.addEdge(edge.getVertex1(), edge.getVertex2());
			//System.out.println(e);
			
			graph.setEdgeWeight(e, tagMap.get(edge));
			
		}
	}*/
	/*get the edge number including the weight*/
	public int getEdgeNumber(){
		int edgeNumber = 0;
		for (VertexPair p: this.tagMap.keySet())
			edgeNumber += this.tagMap.get(p);
		return edgeNumber;
	}
	public float getDensity(){
		float density;
		int sumEdgeWeight = 0;
		for (DefaultWeightedEdge e: this.graph.edgeSet()){
			sumEdgeWeight+= this.graph.getEdgeWeight(e);
		}
		density = (float) sumEdgeWeight/(float) this.graph.vertexSet().size();
		return density;
	}
	public void copyFrom(TweetGraph that){
		//this.graph = new ListenableUndirectedWeightedGraph<String, DefaultWeightedEdge>(that.graph); 
		this.graph  = new ListenableUndirectedWeightedGraph<String, DefaultWeightedEdge>
	            (DefaultWeightedEdge.class); 
		//a stupid way to copy
		for (String v:that.graph.vertexSet())
			this.graph.addVertex(v);
		for (DefaultWeightedEdge e: that.graph.edgeSet()){
			DefaultWeightedEdge edge = this.graph.addEdge(that.graph.getEdgeSource(e), that.graph.getEdgeTarget(e));
			this.graph.setEdgeWeight(edge, that.graph.getEdgeWeight(e));
		}
	}
	
	public String getMinDegreeVertex(){
		String minDegreeVertex = null;
		int minDegree = -1;
		for (String v: this.graph.vertexSet()){
			int degree = 0;
			for (DefaultWeightedEdge e: this.graph.edgesOf(v)){
				degree += this.graph.getEdgeWeight(e);
			}
			if((minDegreeVertex == null)||(degree<minDegree)){
				minDegreeVertex = v;
				minDegree = degree;
			}
			
		}
		//System.out.println("minDegreeVertex: "+ minDegreeVertex);
		return minDegreeVertex;
	}
	
	public TweetGraph getDensestSubgraph(){
		TweetGraph H = new TweetGraph(this.graph);
		float HDensity = H.getDensity();
		System.out.println("original density: "+ HDensity);
		while(this.graph.vertexSet().size()>1){
			this.removeVertexAndEdges(this.getMinDegreeVertex());	// this.removeVertex is different from graph.removeVertex. 
															//this.removeVertex can also remove the connected edges
			float newDensity = this.getDensity();
			System.out.println("Hdensity: "+HDensity+" new density: "+ newDensity+"HSIZE: "+H.graph.vertexSet().size());
			if(newDensity>HDensity){
				H.copyFrom(this);
				System.out.println("Vertex number: "+H.graph.vertexSet().size());
				HDensity = newDensity;
			}
			//let v be the vertex with minimum degree in this.graph
			//remove v and all connected edges from this.graph
			//if this.getDensity() > H.getDensity() then H.copyFrom(this)
		}
		System.out.println("Output Vertex number: "+H.graph.vertexSet().size());
		return H;
	}
	
	public static void main(String[] args) throws IOException{
		String file ="/Volumes/SSD disk Yiru/saclay/distributed data mining & machine learning/TP/NewYorkOneWeek/NewYork-2015-2-23";
		//String file = "/Volumes/SSD disk Yiru/saclay/workspace/DDMaML/src/miningTweet/one_tweet.txt";
		TweetFilter tf = new TweetFilter(file);
		TweetGraph tg = new TweetGraph();
		tg.getTagMap(tf.filter());
		//tg.buildGraph();
		System.out.println("original size: "+tg.vertexSet.size()+" "+tg.tagMap.size());
		TweetGraph densetG = tg.getDensestSubgraph();
		System.out.println("subgraph size: "+densetG.graph.vertexSet().size()+" "+densetG.graph.edgeSet().size());
		//tg.graph.get
		/*for (Entry<VertexPair, Integer> element:tg.tagMap.entrySet())
			if (element.getValue()>5)
				System.out.println(element.getKey().toString()+":"+element.getValue());
				*/
		//System.out.println(tg.graph.toString());
	}
	
}
