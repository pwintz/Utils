package paul.wintz.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import paul.wintz.utils.TypeMap;

public class TypeMapTest {

	private static TypeMap<Interface> typeMap = new TypeMap<>(StaticClass.class, InnerClass.class);

	private interface Interface {
		int getNumber();
	}

	public static class StaticClass implements Interface{
		int number = 1;

		@Override
		public int getNumber() {
			return number;
		}
	}

	public class InnerClass implements Interface{
		int number = 1;

		@Override
		public int getNumber() {
			return number;
		}
	}

	@Test
	public final void testAdd() {
		typeMap.add(StaticClass.class);
	}

	@Test
	public final void testInstantiateStatic() {
		final Interface a = typeMap.instantiate(StaticClass.class.getSimpleName(), this);
		assertEquals(1, a.getNumber());
	}

	@Test
	public final void testInstantiateInner(){
		final Interface a = typeMap.instantiate(InnerClass.class.getSimpleName(), this);
		assertEquals(1, a.getNumber());
	}

}
