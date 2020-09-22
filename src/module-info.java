module codingclub {
	requires java.base;
	requires javafx.graphics;
	requires transitive javafx.controls;
	requires javafx.base;
	
	exports primitivevisualizer;
	exports typevisualizer;
}