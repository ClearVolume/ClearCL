package clearcl.backend;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import clearcl.ClearCLPeerPointer;
import clearcl.enums.HostAccessType;
import clearcl.enums.ImageChannelOrder;
import clearcl.enums.ImageChannelType;
import clearcl.enums.ImageType;
import clearcl.enums.KernelAccessType;
import clearcl.enums.BuildStatus;
import clearcl.enums.DeviceType;
import coremem.ContiguousMemoryInterface;

public interface ClearCLBackendInterface
{

	int getNumberOfPlatforms();

	ClearCLPeerPointer getPlatformIds(int pPlatformIndex);

	String getPlatformName(ClearCLPeerPointer pPlatformIdPointer);

	int getNumberOfDevicesForPlatform(ClearCLPeerPointer pPlatformPointer,
																		DeviceType pDeviceType);

	int getNumberOfDevicesForPlatform(ClearCLPeerPointer pPlatformPointer);

	ClearCLPeerPointer getDeviceId(	ClearCLPeerPointer pPlatformPointer,
																	DeviceType pDeviceType,
																	int pDeviceIndex);

	ClearCLPeerPointer getDeviceId(	ClearCLPeerPointer pPlatformPointer,
																	int pDeviceIndex);

	String getDeviceName(ClearCLPeerPointer pDevicePointer);

	DeviceType getDeviceType(ClearCLPeerPointer pDevicePointer);

	String getDeviceVersion(ClearCLPeerPointer pDevicePointer);

	ClearCLPeerPointer getContext(ClearCLPeerPointer pPlatformPointer,
																ClearCLPeerPointer... pDevicePointers);

	ClearCLPeerPointer createQueue(	ClearCLPeerPointer pDevicePointer,
																	ClearCLPeerPointer pContextPointer,
																	boolean pInOrder);

	ClearCLPeerPointer createBuffer(ClearCLPeerPointer pContextPointer,
																	HostAccessType pHostAccessType,
																	KernelAccessType pKernelAccessType,
																	long pBufferSizeInBytes);

	ClearCLPeerPointer createImage(	ClearCLPeerPointer pContextPointer,
																	HostAccessType pHostAccessType,
																	KernelAccessType pKernelAccessType,
																	ImageType pImageType,
																	ImageChannelOrder pImageChannelOrder,
																	ImageChannelType pImageChannelType,
																	long pWidth,
																	long pHeight,
																	long pDepth);

	ClearCLPeerPointer createProgram(	ClearCLPeerPointer pContextPointer,
																		String... pSourceCode);

	void buildProgram(ClearCLPeerPointer pProgramPointer,
										String pOptions);

	BuildStatus getBuildStatus(	ClearCLPeerPointer pDevicePointer,
															ClearCLPeerPointer pProgramPointer);

	String getBuildLog(	ClearCLPeerPointer pDevicePointer,
											ClearCLPeerPointer pProgramPointer);

	ClearCLPeerPointer createKernel(ClearCLPeerPointer pProgramPointer,
																	String pKernelName);

	void setKernelArgument(	ClearCLPeerPointer pPointer,
													int pIndex,
													Object pObject);

	void enqueueKernelExecution(ClearCLPeerPointer pQueuePointer,
															ClearCLPeerPointer pKernelPointer,
															int pDimensions,
															long[] pGlobalOffsets,
															long[] pGlobalSizes,
															long[] pLocalSize);

	void releaseBuffer(ClearCLPeerPointer pPeerPointer);

	void releaseContext(ClearCLPeerPointer pPeerPointer);

	void releaseDevice(ClearCLPeerPointer pPeerPointer);

	void releaseImage(ClearCLPeerPointer pPeerPointer);

	void releaseKernel(ClearCLPeerPointer pPeerPointer);

	void releaseProgram(ClearCLPeerPointer pPeerPointer);

	void releaseQueue(ClearCLPeerPointer pPeerPointer);

	void enqueueReadFromBuffer(	ClearCLPeerPointer pQueuePointer,
															ClearCLPeerPointer pBufferPointer,
															boolean pBlockingRead,
															long pOffsetInBuffer,
															long pLengthInBuffer,
															ClearCLPeerPointer pHostMemPointer);

	void enqueueWriteToBuffer(ClearCLPeerPointer pQueuePointer,
														ClearCLPeerPointer pBufferPointer,
														boolean pBlockingWrite,
														long pOffsetInBuffer,
														long pLengthInBytes,
														ClearCLPeerPointer pHostMemPointer);
	
	void enqueueReadFromBufferBox(ClearCLPeerPointer pQueuePointer,
																ClearCLPeerPointer pBufferPointer,
																boolean pBlockingRead,
																long[] pBufferOrigin,
																long[] pHostOrigin,
																long[] pRegion,
																ClearCLPeerPointer pHostMemPointer);

	void enqueueWriteToBufferBox(	ClearCLPeerPointer pQueuePointer,
																ClearCLPeerPointer pBufferPointer,
																boolean pBlockingWrite,
																long[] pBufferOrigin,
																long[] pHostOrigin,
																long[] pRegion,
																ClearCLPeerPointer pHostMemPointer);

	void enqueueFillBuffer(	ClearCLPeerPointer pQueuePointer,
													ClearCLPeerPointer pBufferPointer,
													boolean pBlockingFill,
													long pOffsetInBytes,
													long pLengthInBytes,
													byte[] pPattern);

	void enqueueCopyBuffer(	ClearCLPeerPointer pQueuePointer,
													ClearCLPeerPointer pSrcBufferPointer,
													ClearCLPeerPointer pDstBufferPointer,
													boolean pBlockingCopy,
													long pSrcOffsetInBytes,
													long pDstOffsetInBytes,
													long pLengthToCopyInBytes);

	void enqueueCopyBufferBox(ClearCLPeerPointer pQueuePointer,
														ClearCLPeerPointer pSrcBufferPointer,
														ClearCLPeerPointer pDstBufferPointer,
														boolean pBlockingCopy,
														long[] pSrcOrigin,
														long[] pDstOrigin,
														long[] pRegion);

	void enqueueCopyBufferToImage(ClearCLPeerPointer pQueuePointer,
																ClearCLPeerPointer pSrcBufferPointer,
																ClearCLPeerPointer pDstImagePointer,
																boolean pBlockingCopy,
																long pSrcOffsetInBytes,
																long[] pDstOrigin,
																long[] pDstRegion);

	void enqueueReadFromImage(ClearCLPeerPointer pQueuePointer,
														ClearCLPeerPointer pImagePointer,
														boolean pReadWrite,
														long[] pOrigin,
														long[] pRegion,
														ClearCLPeerPointer pHostMemPointer);

	void enqueueWriteToImage(	ClearCLPeerPointer pQueuePointer,
														ClearCLPeerPointer pImagePointer,
														boolean pBlockingWrite,
														long[] pOrigin,
														long[] pRegion,
														ClearCLPeerPointer pHostMemPointer);

	void enqueueFillImage(ClearCLPeerPointer pQueuePointer,
												ClearCLPeerPointer pImagePointer,
												boolean pBlockingFill,
												long[] pOrigin,
												long[] pRegion,
												byte[] pColor);

	void enqueueCopyImage(ClearCLPeerPointer pQueuePointer,
												ClearCLPeerPointer pSrcBImagePointer,
												ClearCLPeerPointer pDstImagePointer,
												boolean pBlockingCopy,
												long[] pSrcOrigin,
												long[] pDstOrigin,
												long[] pRegion);

	void enqueueCopyImageToBuffer(ClearCLPeerPointer pQueuePointer,
																ClearCLPeerPointer pSrcImagePointer,
																ClearCLPeerPointer pDstBufferPointer,
																boolean pBlockingCopy,
																long[] pSrcOrigin,
																long[] pSrcRegion,
																long pDstOffset);

	ClearCLPeerPointer wrap(Buffer pBuffer);

	ClearCLPeerPointer wrap(ContiguousMemoryInterface pContiguousMemory);

	void waitQueueToFinish(ClearCLPeerPointer pPeerPointer);



}
