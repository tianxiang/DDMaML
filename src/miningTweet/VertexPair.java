package miningTweet;

public class VertexPair extends Object{
	private String vertex1;
	private String vertex2;
	public VertexPair(String v1, String v2){
		//ensure that fist string is bigger than the second one
		
		if (v1.compareTo(v2)>=0){
			this.vertex1 = v1;
			this.vertex2 = v2;
		}else{
			this.vertex1 = v2;
			this.vertex2 = v1;
		}
	}
	public boolean contains(String vertex){
		if (this.vertex1.equals(vertex) || this.vertex2.equals(vertex))
			return true;
		else return false;
	}
	//Override method equals so that containsKey() can work
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		else{
			VertexPair vp = (VertexPair) o;
		if (this.vertex1.equals(vp.vertex1)){
			if(this.vertex2.equals(vp.vertex2))
				return true;
			else return false;
		}else if(this.vertex1.equals(vp.vertex2)){
			if(this.vertex2.equals(vp.vertex1))
				return true;
			else return false;
		}else return false;
		}
		
	}
	public String getVertex1(){
		return this.vertex1;
	}
	public String getVertex2(){
		return this.vertex2;
	}
	//Override method hashCode so that containsKey() can work
	@Override 
	public int hashCode(){
		return this.vertex1.hashCode() + this.vertex2.hashCode();
	}
	@Override
	public String toString(){
		return vertex1+", "+vertex2;
	}
}
