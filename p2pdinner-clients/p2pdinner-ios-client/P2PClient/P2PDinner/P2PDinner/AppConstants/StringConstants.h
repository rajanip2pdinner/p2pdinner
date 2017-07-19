//
//  StringConstants.h
//  P2PDinner
//
//  Created by sudheerkumar on 2/6/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#ifndef P2PDinner_StringConstants_h
//GuestTableViewCell Begin
#define OneGuest @"1 Guest"
#define MoreThanGuest @"%d Guests"
//GuestTableViewCell end
#define P2PDinner_StringConstants_h

#pragma mark- MyOrderDetailCell
#define kAlert_Title_Access_Error @"P2P Access Error"
#define kAlert_Message_Access_Error @"Please enable settings privacy allow to access calender"
#define kAlert_Ok @"Ok"

#define kAlert_Title_EventAdded @"P2P Event Added"
#define kAlert_Message_EventAdded @"Added into calendar"

#define kMy_P2PDinner_Text @"My P2PDinner"

#pragma mark - AgreementsViewController
#define kLoadingText @"Loading..."

#pragma mark - SettingsViewController

#define kTitle_Settings @"Settings"
#define kSegueID_AgreementVC @"AgreementViewController"
#define kTerms_URL @"https://p2pdinner-services.herokuapp.com/legal/Terms.html"
#define kCopyright_URL @"https://p2pdinner-services.herokuapp.com/legal/copyright.html"
#define kPrivacy_URL @"https://p2pdinner-services.herokuapp.com/legal/privacy.html"
#define kTermsConditionText @"Terms & Conditions"
#define kCopyrightText @"Copyright Agreement"
#define kSafetyProfile @"Food Safety"
#define kPrivacyText @"Privacy policy"

#pragma mark - StarRatingView

#define kRating_Text @"%d%%"
#define kBackGround_Image @"5starsgray"


#pragma mark - SplNeedsController

#define kFont_Name @"Plantin"
#define kSpecial_Needs @"Special Needs"
#define kSplNeedsCell @"SplNeedsCell"
#define kVegan @"Vegan"
#define kKosher @"Kosher"
#define kVegetarian @"Vegetarian"
#define kGluten_Free @"Gluten Free"
#define kHalal @"Halal"
#define kDiabetic @"Diabetic"
#define kLow_Carb @"Low Carb"
#define kLow_Fat_Diet @"Low Fat Diet"

#pragma mark - SplNeedsController

#define kCost @"Cost"
#define kCostCell @"CostCell"
#define kMaximumCell @"MaximumCell"


#pragma mark - CostCell

#define kdollars @"dollars"
#define kNumeric_CharSet @"0123456789."
#define kCostPerPlate @"Cost Per Plate (%@)"

#pragma mark - PlaceViewController

#define kDescription @"description"
#define kLocalizaPatten @"en_%@"
#define kPlace @"Place"
#define kAddressCell @"AddressCell"
#define kDeliveryOptionsCell @"DeliveryOptionsCell"
#define kEat_In @"Eat-In"
#define kTo_Go @"To-Go"
#define kDone @"Done"
#define kEdit @"Edit"

#pragma mark - DateAndTimeController

#define kAleartContent @"\n\n\n\n\n\n\n\n\n\n\n\n"
#define kOK @"OK"

#pragma mark - AddTimeController

#define kDateAndTimeFormat @"MM/dd/yyyy HH:mm:ss"
#define kDateAndTime12hrsFormat @"MM/dd/yyyy h.mm a"
#define kDateMonthYearOnly @"MM/dd/yyyy"
#define kTimeMinSecWithColon @"HH:mm:ss"
#define kTimeMinSecZeroWithColon @"HH:mm:00"
#define kTimeMinOnly @"h.mm"
#define kTimeMinOnly12hrsFormat @"h.mm a"
#define kTimeMinOnly12hrsFormat2Digit @"hh:mm a"
#define kTodayWithDate @"Today(%@)"
#define k2StringAppendFormart @"%@%@"
#define k2StringAppendFormartWithSpace @"%@ %@"
#define kDinner_Listing @"Dinner Listing"
#define kTime @"Time"
#define kDateSelectCell @"DateSelectCell"
#define kTimeSelectCell @"TimeSelectCell"
#define kTimeSelectCell1 @"TimeSelectCell1"
#define kAcceptOrdersCell @"AcceptOrdersCell"

#pragma mark - AcceptOrdersTableCell

#define kUTC @"UTC"
#define kAMPM @"a"
#define kDot_String @"."
#define kEmpty_String @""
#define kSpace_String @" "
#define kAppendWithDot_String @" %@."
#define kSrtingPatten @"%@"
#define kSrtingPattenWithSpace @" %@ "
#define kAppendWithComaString @"%@,"
#define k2StringAppendFormartWithComa @"%@,%@"
#define kNextLaine @"\n"

#pragma mark - AddFoodPhotos

#define kSlash_String @"\""
#define kComa_String @","
#define kPhotos @"Photos"
#define kLoading @"Loading.."
#define kNoteCell @"NoteCell"
#define kAddPhotoCell @"AddPhotoCell"
#define kAddPhotoImageName @"addPhoto"
#define kselvam @"selvam"
#define kAdd_Dinner_From @"Add Dinner photo from?"
#define kCancel @"Cancel"
#define kCamera @"Camera"
#define kChoose_Photos @"Choose from Photos"
#define kUploading @"Uploading.."
#define kImagePicker @"UIImagePickerControllerOriginalImage"
#define kUploadError @"Upload Error"
#define kCertUploadError @"Certificate upload error please contact support."


#pragma mark - CreateFoodItem

#define kAddTime @"AddTime"
#define kCreatFoodItem @"Create Food Item"
#define kTitleCell @"titleCell"
#define kcategoryCell @"categoryCell"
#define kSelect_Category @"Select Category.."
#define kDescriptionCell @"descriptionCell"
#define kCategorySelectController @"CategorySelectController"


#pragma mark - CategorySelectController

#define kCategoryCell @"CetegoryCell"
#define kname @"name"
#define kPredicateOther @"name CONTAINS[cd] 'Other'"

#pragma mark - MyOrderViewController

#define kMyOrders @"My Orders"
#define kConf @"Conf# "
#define kForPlates @" for %ld Plates"
#define kMyOrdersCell @"MyOrdersCell"
#define kMyOrderDetailCell @"MyOrderDetailCell"
#define kMyOrdersCellLoading @"MyOrdersCellLoading"
#define knoImage @"noImage"
#define kServingTo @"Serving %@ to %@"
#define kServingBetween @"Served between %@ and %@"
#define kfoodSaftyNote @"Govt. food safety Approval(s) [Required before selling] Load Photos of Certificates:"
#endif
