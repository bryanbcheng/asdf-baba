class UserController < ApplicationController
  protect_from_forgery :only => [:protected_transfer, :protected_post_transfer]
  
  def transfer
    if not logged_in?
      render "main/must_login"
      return
    end
    
    @user = params[:user]
    @amount = params[:quantity]
    render :transfer_form
  end
  
  def post_transfer 
    if not logged_in?
      render "main/must_login"
      return
    end
    
    destination_username = params[:destination_username]
    @quantity = params[:quantity].to_i
    
    @error = ""
    @source_user = @logged_in_user
    @destination_user = User.find_by_username(destination_username)
    if not @destination_user
      @error = "The recipient does not exist."
    elsif @source_user.bitbars < @quantity
      @error = "You do not have enough bitbars!"
    end

    csrf_token = params[:csrf_token]
    if csrf_token != get_csrf_token
      @error = "Invalid CSRF token"
    end
    
    if @error != ""
      render :transfer_form
    else
      @source_user.bitbars -= @quantity
      @destination_user.bitbars += @quantity
      @source_user.save
      @destination_user.save
      render :transfer_success
    end 
  end
  
  # Exact same as above, but Rails auto-CSRF protection enabled on it
  def protected_transfer
    transfer
  end
  def protected_post_transfer 
    post_transfer
  end
  
  def view_profile
    @username = params[:username]
    @user = User.find_by_username(@username)
    if not @user
      if @username and @username != ""
        @error = "User #{@username} not found"
      elsif logged_in?
        @user = @logged_in_user
      end
    end
    
    render :profile
  end
  
  def set_profile
    if not logged_in?
      render "main/must_login"
      return
    end
    
    @logged_in_user.profile = params[:new_profile]
    @logged_in_user.save
    
    render :set_profile_success
  end
end
