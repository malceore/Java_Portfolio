public class Dog {
	String breed;
	int age;
	String color;

	// Here is a constructor so you can get and set values.
	//public Puppy(String name) {
		// This constructor has one parameter, name.
		//System.out.println("Name chosen is :" + name );
	//}

	void barking(){
		System.out.println("Bork Bark Bark!");
	}

	void sleeping(){
		System.out.println("...zzzzZZZ");
	}

	public static void main(String[] args){
		Dog testdog = new Dog();
		testdog.barking();
		testdog.sleeping();
	}
}
