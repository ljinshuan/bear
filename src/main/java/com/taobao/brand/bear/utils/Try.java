package com.taobao.brand.bear.utils;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author 八筏
 */
public class Try {

	public static Runnable rof(UncheckedRuntime mapper) {
        Objects.requireNonNull(mapper);
        return () -> {
            try {
                mapper.run();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    public static Runnable rof(UncheckedRuntime mapper,Consumer<Exception> eh) {
        Objects.requireNonNull(mapper);
        return () -> {
            try {
                mapper.run();
            } catch (Exception ex) {
                eh.accept(ex);
            }
        };
    }

    public static <T> Supplier<T> sof(UncheckedSupplier<T> mapper) {
        Objects.requireNonNull(mapper);
        return () -> {
            try {
                return mapper.get();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    public static <T> Supplier<T> sof(UncheckedSupplier<T> mapper,Consumer<Exception> eh) {
        Objects.requireNonNull(mapper);
        return () -> {
            try {
                return mapper.get();
            } catch (Exception ex) {
                eh.accept(ex);
                return null;
            }
        };
    }

	public static <T> Consumer<T> cof(UncheckedConsumer<T> mapper) {
		Objects.requireNonNull(mapper);
		return t -> {
			try {
				mapper.accept(t);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}
	
	public static <T> Consumer<T> ncof(UncheckedConsumer<T> mapper) {
		Objects.requireNonNull(mapper);
		return t -> {
			try {
				mapper.accept(t);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		};
	}
	
	public static <T, R> Function<T, R> of(UncheckedFunction<T, R> mapper) {
		Objects.requireNonNull(mapper);
		return t -> {
			try {
				return mapper.apply(t);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}

	public static <T, R> Function<T, R> of(UncheckedFunction<T, R> mapper,Consumer<Exception> eh) {
		Objects.requireNonNull(mapper);
		return t -> {
			try {
				return mapper.apply(t);
			} catch (Exception ex) {
				eh.accept(ex);
				return null;
			}
		};
	}
	
	public static <T, R> Function<T, R> eof(UncheckedFunction<T, R> mapper, Exception cex) {
		Objects.requireNonNull(mapper);
		return t -> {
			try {
				return mapper.apply(t);
			} catch (Exception ex) {
				if(cex!=null&&cex.getClass()==ex.getClass()){
					throw new RuntimeException(cex);
				}else{
					throw new RuntimeException(ex);
				}
			}
		};
	}
	

	@FunctionalInterface
	public static interface UncheckedFunction<T, R> {
		R apply(T t) throws Exception;
	}
	
	@FunctionalInterface
	public static interface UncheckedConsumer<T> {
		void accept(T t) throws Exception;
	}

    @FunctionalInterface
    public static interface UncheckedRuntime {
        void run() throws Exception;
    }

    @FunctionalInterface
    public static interface UncheckedSupplier<T> {
        T get() throws Exception;
    }
	
//	public static void main(String[] args) throws Exception {
//		long count = Files.walk(Paths.get("D:/tmp")) // 递归获得项目目录下的所有文件
//		        .filter(file -> !Files.isDirectory(file)) // 筛选出文件
//		        .filter(file -> file.toString().endsWith(".html")) // 筛选出 java 文件
//		        .flatMap(Try.of(file -> Files.lines(file), Stream.empty())) // 将 会抛出受检异常的 Lambda 包装为 抛出非受检异常的 Lambda
//		        .filter(line -> !line.trim().isEmpty()) // 过滤掉空行
//		        .count();
//		System.out.println("代码行数：" + count);
//	}
}