package com.cs155.trustedapp;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IGetContactsString extends IInterface
{
    public abstract String GetContacts(String paramString)
	throws RemoteException;

  public static abstract class Stub extends Binder
    implements IGetContactsString
  {
      private static final String DESCRIPTOR = "com.cs155.trustedapp.IGetContactsString";
      static final int TRANSACTION_GetContacts = 1;

      public Stub()
      {
	  attachInterface(this, "com.cs155.trustedapp.IGetContactsString");
      }

      public static IGetContactsString asInterface(IBinder paramIBinder)
      {
	  if (paramIBinder == null)
	      return null;
	  IInterface localIInterface = paramIBinder.queryLocalInterface("com.cs155.trustedapp.IGetContactsString");
	  if ((localIInterface != null) && ((localIInterface instanceof IGetContactsString)))
	      return (IGetContactsString)localIInterface;
	  return new Proxy(paramIBinder);
      }

      public IBinder asBinder()
      {
	  return this;
      }

      public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
      {
	  switch (paramInt1)
	      {
	      default:
		  return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
	      case 1598968902:
		  paramParcel2.writeString("com.cs155.trustedapp.IGetContactsString");
		  return true;
	      case 1:
	      }
	  paramParcel1.enforceInterface("com.cs155.trustedapp.IGetContactsString");
	  String str = GetContacts(paramParcel1.readString());
	  paramParcel2.writeNoException();
	  paramParcel2.writeString(str);
	  return true;
      }

    private static class Proxy
      implements IGetContactsString
    {
	private IBinder mRemote;

	Proxy(IBinder paramIBinder)
	{
	    this.mRemote = paramIBinder;
	}

	public String GetContacts(String paramString)
        throws RemoteException
	{
	    Parcel localParcel1 = Parcel.obtain();
	    Parcel localParcel2 = Parcel.obtain();
        try
	    {
		localParcel1.writeInterfaceToken("com.cs155.trustedapp.IGetContactsString");
		localParcel1.writeString(paramString);
		this.mRemote.transact(1, localParcel1, localParcel2, 0);
		localParcel2.readException();
		String str = localParcel2.readString();
		return str;
	    }
        finally
	    {
		localParcel2.recycle();
		localParcel1.recycle();
	    }
	}

	public IBinder asBinder()
	{
	    return this.mRemote;
	}

	public String getInterfaceDescriptor()
	{
	    return "com.cs155.trustedapp.IGetContactsString";
	}
    }
  }
}