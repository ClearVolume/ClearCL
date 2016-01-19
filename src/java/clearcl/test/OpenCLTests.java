package clearcl.test;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.nativelibs4java.opencl.CLBuffer;
import com.nativelibs4java.opencl.CLImage3D;
import com.nativelibs4java.opencl.CLImageFormat;
import com.nativelibs4java.opencl.CLKernel;

import clearcl.OpenCLAvailability;
import clearcl.OpenCLDevice;

public class OpenCLTests
{

	@Test
	public void test_creation()
	{
		if (!OpenCLAvailability.isOpenCLAvailable())
			return;

		try
		{
			final OpenCLDevice dev = new OpenCLDevice();
			dev.initCL();
			dev.printInfo();

			final int N = 512;

			// create the buffer/image type we would need for the renderer

			final CLBuffer<Float> clBufIn = dev.createInputFloatBuffer(N);
			final CLBuffer<Integer> clBufOut = dev.createOutputIntBuffer(N);

			final CLImage3D img = dev.createGenericImage3D(	N,
															N,
															N,
															CLImageFormat.ChannelOrder.R,
															CLImageFormat.ChannelDataType.SignedInt16);
		}
		catch (final Throwable e)
		{

			fail();
			e.printStackTrace();
		}

	}

	@Test
	public void test_compile()
	{
		if (!OpenCLAvailability.isOpenCLAvailable())
			return;

		try
		{
			final OpenCLDevice lOpenCLDevice = new OpenCLDevice();
			lOpenCLDevice.initCL();
			lOpenCLDevice.printInfo();

			final CLKernel lCLKernel = lOpenCLDevice.compileKernel(	OpenCLTests.class.getResource("kernels/test.cl"),
																	"test_char");
		}
		catch (final Throwable e)
		{
			e.printStackTrace();
			fail();
		}

	}

	@Test
	public void test_run()
	{
		if (!OpenCLAvailability.isOpenCLAvailable())
			return;

		try
		{
			final OpenCLDevice lOpenCLDevice = new OpenCLDevice();
			lOpenCLDevice.initCL();
			lOpenCLDevice.printInfo();
			final int N = 100;

			final CLKernel lCLKernel = lOpenCLDevice.compileKernel(	OpenCLTests.class.getResource("kernels/test.cl"),
																	"test_float");

			final CLBuffer<Float> clBufIn = lOpenCLDevice.createOutputFloatBuffer(N);

			lCLKernel.setArgs(clBufIn, N);

			lOpenCLDevice.run(lCLKernel, N);

		}
		catch (final Throwable e)
		{
			e.printStackTrace();
			fail();
		}

	}
}