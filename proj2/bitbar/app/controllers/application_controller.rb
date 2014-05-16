class ApplicationController < ActionController::Base
  before_filter :load_logged_in_user
  before_filter :disable_protections
  before_filter :check_security_settings
  
  helper_method :get_csrf_token

  # Since this is before_filter in the ApplicationController, this will run for every controller. If the user is logged in, their record will be in the @logged_in_user variable. Otherwise, that variable will be null.
  def load_logged_in_user
      @logged_in_user = User.find_by_id(session[:logged_in_id])
  end
  
  def logged_in?
      return @logged_in_user != nil
  end
  
  def disable_protections
    # This disables some browser built-in XSS protection. For the fixes portion of the lab, these MUST stay disabled. You should fix the vulnerabilities some other way...
    response.headers['X-XSS-Protection'] = '0'
    response.headers.except! 'X-Frame-Options'
  end
  
  def check_security_settings 
    # These settings can be used to selectively disable security measures for a particular part. 
    # You may not use them unless explicitly stated in the problem description, and you may not use
    # them at all for the defenses part of the assignment. 
    @disable_framebusting = params['disable_fb'] == 'yes'
  end

  require 'openssl'
  def get_csrf_token
    key = "6e8fdb57969f07f30a4b039f4a606dbc54f7972001747133cb99a520d48acb34"
    data = session[:session_id]
    token = OpenSSL::HMAC.hexdigest(OpenSSL::Digest::Digest.new('sha256'), key, data)
  end 

end
